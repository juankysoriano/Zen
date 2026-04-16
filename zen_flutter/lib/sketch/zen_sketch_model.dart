part of '../main.dart';

class ZenSketchModel extends ChangeNotifier {
  final math.Random _random = math.Random();
  final List<DrawCommand> _commands = [];
  final List<Branch> _branches = [];
  final List<SketchSnapshot> _undoStack = [];
  ZenAssets? assets;
  BrushColor brushColor = BrushColor.dark;
  BrushSize brushSize = BrushSize.medium;
  Flower flower = Flower.poppy;
  final FingerPositionSmoother _smoother = FingerPositionSmoother();
  Offset _rawPoint = Offset.zero;
  Offset _previousRawPoint = Offset.zero;
  bool _touching = false;
  bool _reachedRadiusAfterReset = false;
  double _radius = SketchMetrics.absoluteMinRadius;
  int _step = 0;

  Iterable<DrawCommand> get commands => _commands;

  bool get canUndo => _undoStack.isNotEmpty;

  void pointerDown(PointerDownEvent event) {
    _recordSnapshot();
    _touching = true;
    _rawPoint = event.localPosition;
    _previousRawPoint = _rawPoint;
    _smoother.resetTo(_rawPoint);
    _resetRadius();
  }

  void pointerMove(PointerMoveEvent event) {
    if (!_touching) {
      return;
    }
    _splitAndProcessMove(event.localPosition);
    notifyListeners();
  }

  void pointerUp(PointerUpEvent event) {
    _rawPoint = event.localPosition;
    _previousRawPoint = _rawPoint;
    _smoother.resetTo(event.localPosition);
    _touching = false;
    notifyListeners();
  }

  void pointerCancel(PointerCancelEvent event) {
    _touching = false;
    notifyListeners();
  }

  void tick() {
    if (assets == null) {
      return;
    }
    if (!_hasToSkipStep() && flower != Flower.none) {
      _paintAndUpdateBranches();
    }
    _attemptToBloomBranch();
    _step++;
    if (_branches.isNotEmpty || _touching) {
      notifyListeners();
    }
  }

  void clear() {
    _commands.clear();
    _branches.clear();
    _undoStack.clear();
    notifyListeners();
  }

  void undo() {
    if (_undoStack.isEmpty) {
      return;
    }
    final snapshot = _undoStack.removeLast();
    _commands
      ..clear()
      ..addAll(snapshot.commands);
    _branches
      ..clear()
      ..addAll(snapshot.branches);
    notifyListeners();
  }

  void _recordSnapshot() {
    _undoStack.add(
      SketchSnapshot(
        List<DrawCommand>.of(_commands),
        _branches.map((branch) => branch.clone()).toList(),
      ),
    );
    if (_undoStack.length > 25) {
      _undoStack.removeAt(0);
    }
  }

  void _splitAndProcessMove(Offset point) {
    final start = _rawPoint;
    final diff = point - start;
    for (var i = 1; i <= SketchMetrics.inputDivisions; i++) {
      _processMove(start + diff * (i / SketchMetrics.inputDivisions));
    }
  }

  void _processMove(Offset point) {
    _previousRawPoint = _rawPoint;
    _rawPoint = point;
    _smoother.moveTo(point);
    final width = _targetStrokeWidth();
    _commands.add(
      LineCommand(
        _smoother.oldPosition,
        _smoother.position,
        brushColor.color,
        width,
        1,
      ),
    );
    _paintInkTexture(_smoother.position, width);
    _updateRadius();
  }

  double _targetStrokeWidth() {
    if (brushColor == BrushColor.erase) {
      return maxRadius * 0.5;
    }
    return _radius * 0.5;
  }

  void _paintInkTexture(Offset point, double width) {
    final brushInk = assets?.brushInk;
    if (brushColor != BrushColor.erase &&
        brushInk != null &&
        _random.nextInt(100) > 98) {
      _commands.add(
        ImageCommand(
          image: brushInk,
          center: point + Offset(_randomInRange(-3, 3), _randomInRange(-3, 3)),
          size: Size.square(_radius * _randomInRange(0.65, 1.35)),
          rotation: _random.nextDouble() * math.pi * 2,
          tint: brushColor.color.withValues(alpha: _randomInRange(0.3, 0.75)),
          flipX: _random.nextBool(),
        ),
      );
    }
    if (brushColor != BrushColor.erase && _random.nextInt(100) > 94) {
      _commands.add(
        InkBleedCommand(
          center:
              point +
              Offset(
                _randomInRange(-width, width),
                _randomInRange(-width, width),
              ),
          radius: _randomInRange(width * 0.12, width * 0.38),
          color: brushColor.color,
          opacity: _randomInRange(0.05, 0.16),
        ),
      );
    }
  }

  void _updateRadius() {
    if (_isFingerMoving() &&
        _fingerVelocity < SketchMetrics.velocityThreshold) {
      _radius += SketchMetrics.inkDropStep;
    } else {
      _radius -= SketchMetrics.inkDropStep;
    }
    _reachedRadiusAfterReset =
        _reachedRadiusAfterReset || _radius >= brushSize.radius / 2;
    _radius = _radius.clamp(_minimumRadius, maxRadius);
  }

  void _resetRadius() {
    _reachedRadiusAfterReset = false;
    _radius = SketchMetrics.absoluteMinRadius;
  }

  double get _minimumRadius {
    if (_reachedRadiusAfterReset || !_touching) {
      return brushSize.minRadius;
    }
    return SketchMetrics.absoluteMinRadius;
  }

  double get maxRadius => brushSize.radius;

  bool _hasToSkipStep() => _step % 3 != 0;

  void _paintAndUpdateBranches() {
    final branches = List<Branch>.of(_branches);
    for (final branch in branches) {
      if (branch.isDead) {
        _branches.remove(branch);
        _bloomFlowerIfLuck(branch);
      } else {
        _commands.add(
          LineCommand(
            branch.position,
            branch.previousPosition,
            ZenColors.darkBrush,
            1,
            0.5,
          ),
        );
        branch.update();
        if (_random.nextInt(100) > 90) {
          _bloomFrom(branch);
        }
      }
    }
  }

  void _bloomFlowerIfLuck(Branch branch) {
    if (_random.nextInt(100) > 70) {
      _paintFlowerFor(branch);
    } else {
      _commands.add(
        PointCommand(
          branch.position,
          SketchMetrics.branchDefaultRadius * 2,
          ZenColors.amberBrush,
          0.5,
        ),
      );
    }
  }

  void _paintFlowerFor(Branch branch) {
    final images = assets?.flowerImages(flower) ?? const <ui.Image>[];
    if (images.isEmpty) {
      return;
    }
    _commands.add(
      ImageCommand(
        image: images[_random.nextInt(images.length)],
        center: branch.position,
        size: Size.square(_randomInRange(flower.minSize, flower.maxSize)),
        rotation: _random.nextDouble() * math.pi / 4,
        tint: Colors.white,
        flipX: _random.nextBool(),
      ),
    );
  }

  void _attemptToBloomBranch() {
    final threshold = _fingerVelocity > SketchMetrics.velocityThreshold
        ? 63
        : 83;
    if (brushColor != BrushColor.erase &&
        _touching &&
        _random.nextInt(100) > threshold) {
      final radius = _radius / 4;
      final offset = Offset(
        _horizontalDirection >= 0 ? radius : -radius,
        _verticalDirection >= 0 ? radius : -radius,
      );
      _bloomFrom(Branch.createAt(_smoother.position + offset, _random));
    }
  }

  void _bloomFrom(Branch branch) {
    if (_branches.length < 1000 && branch.canBloom && flower != Flower.none) {
      _branches.add(Branch.createFrom(branch, _random));
    }
  }

  double get _fingerVelocity => _smoother.fingerVelocity;

  bool _isFingerMoving() => (_rawPoint - _previousRawPoint).distance > 0.1;

  int get _horizontalDirection =>
      (_smoother.position.dx - _smoother.oldPosition.dx) >= 0 ? 1 : -1;

  int get _verticalDirection =>
      (_smoother.position.dy - _smoother.oldPosition.dy) >= 0 ? 1 : -1;

  double _randomInRange(double min, double max) {
    return min + _random.nextDouble() * (max - min);
  }
}
