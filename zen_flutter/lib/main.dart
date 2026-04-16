import 'dart:async';
import 'dart:io';
import 'dart:math' as math;
import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:just_audio/just_audio.dart';
import 'package:path_provider/path_provider.dart';
import 'package:share_plus/share_plus.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const ZenApp());
}

class ZenApp extends StatelessWidget {
  const ZenApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Zen',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: ZenColors.primary),
        useMaterial3: true,
      ),
      home: const ZenScreen(),
    );
  }
}

class ZenScreen extends StatefulWidget {
  const ZenScreen({super.key});

  @override
  State<ZenScreen> createState() => _ZenScreenState();
}

class _ZenScreenState extends State<ZenScreen>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  final GlobalKey _canvasKey = GlobalKey();
  late final ZenSketchModel _sketch;
  late final Ticker _ticker;
  late final AudioPlayer _audioPlayer;
  Timer? _musicStepTimer;
  SharedPreferences? _preferences;
  bool _ready = false;
  bool _menuOpen = false;
  bool _musicEnabled = true;
  bool _painting = false;
  double _musicVolume = SketchMetrics.restingMusicVolume;
  int _clearEffectTick = 0;
  ZenTrack _track = ZenTrack.reachingTheSky;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _sketch = ZenSketchModel();
    _audioPlayer = AudioPlayer();
    _ticker = createTicker((_) => _sketch.tick());
    _bootstrap();
  }

  Future<void> _bootstrap() async {
    final prefs = await SharedPreferences.getInstance();
    final assets = await ZenAssets.load();
    _track = _enumValue(
      ZenTrack.values,
      prefs.getInt(Prefs.track),
      ZenTrack.reachingTheSky,
    );
    await _audioPlayer.setAsset(_track.asset);
    await _audioPlayer.setLoopMode(LoopMode.one);
    await _audioPlayer.setVolume(_musicVolume);
    _preferences = prefs;
    _musicEnabled = prefs.getBool(Prefs.musicEnabled) ?? true;
    _sketch
      ..assets = assets
      ..brushColor = _enumValue(
        BrushColor.values,
        prefs.getInt(Prefs.brushColor),
        BrushColor.dark,
      )
      ..brushSize = _enumValue(
        BrushSize.values,
        prefs.getInt(Prefs.brushSize),
        BrushSize.medium,
      )
      ..flower = _enumValue(
        Flower.values,
        prefs.getInt(Prefs.flower),
        Flower.poppy,
      );
    if (_musicEnabled) {
      unawaited(_audioPlayer.play());
    }
    _ticker.start();
    if (mounted) {
      setState(() => _ready = true);
    }
  }

  T _enumValue<T>(List<T> values, int? index, T fallback) {
    if (index == null || index < 0 || index >= values.length) {
      return fallback;
    }
    return values[index];
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.paused ||
        state == AppLifecycleState.inactive) {
      unawaited(_audioPlayer.pause());
    } else if (state == AppLifecycleState.resumed && _musicEnabled) {
      unawaited(_audioPlayer.play());
    }
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    _musicStepTimer?.cancel();
    _ticker.dispose();
    _audioPlayer.dispose();
    _sketch.dispose();
    super.dispose();
  }

  Future<void> _setTrack(ZenTrack track) async {
    final wasPlaying = _musicEnabled;
    _track = track;
    await _preferences?.setInt(Prefs.track, track.index);
    await _audioPlayer.setAsset(track.asset);
    await _audioPlayer.setLoopMode(LoopMode.one);
    await _audioPlayer.setVolume(_musicVolume);
    if (wasPlaying) {
      await _audioPlayer.play();
    }
    if (mounted) {
      setState(() {});
    }
  }

  Future<void> _setBrushColor(BrushColor color) async {
    setState(() => _sketch.brushColor = color);
    await _preferences?.setInt(Prefs.brushColor, color.index);
  }

  Future<void> _setBrushSize(BrushSize size) async {
    setState(() => _sketch.brushSize = size);
    await _preferences?.setInt(Prefs.brushSize, size.index);
  }

  Future<void> _setFlower(Flower flower) async {
    setState(() => _sketch.flower = flower);
    await _preferences?.setInt(Prefs.flower, flower.index);
  }

  Future<void> _toggleMusic() async {
    _musicEnabled = !_musicEnabled;
    await _preferences?.setBool(Prefs.musicEnabled, _musicEnabled);
    if (_musicEnabled) {
      await _audioPlayer.play();
      _startMusicStepper();
    } else {
      _musicStepTimer?.cancel();
      await _audioPlayer.pause();
    }
    if (mounted) {
      setState(() {});
    }
  }

  void _startMusicStepper() {
    _musicStepTimer?.cancel();
    _musicStepTimer = Timer.periodic(SketchMetrics.musicStepInterval, (_) {
      final target = _painting
          ? SketchMetrics.paintingMusicVolume
          : SketchMetrics.restingMusicVolume;
      if ((_musicVolume - target).abs() < SketchMetrics.musicStep) {
        _musicVolume = target;
      } else if (_musicVolume < target) {
        _musicVolume += SketchMetrics.musicStep;
      } else {
        _musicVolume -= SketchMetrics.musicStep;
      }
      _musicVolume = _musicVolume.clamp(
        SketchMetrics.restingMusicVolume,
        SketchMetrics.paintingMusicVolume,
      );
      unawaited(_audioPlayer.setVolume(_musicVolume));
    });
  }

  void _setPainting(bool painting) {
    if (_painting == painting) {
      return;
    }
    _painting = painting;
    if (_musicEnabled) {
      _startMusicStepper();
    }
  }

  void _pointerDown(PointerDownEvent event) {
    setState(() => _sketch.pointerDown(event));
    _setPainting(true);
  }

  void _pointerMove(PointerMoveEvent event) {
    _sketch.pointerMove(event);
  }

  void _pointerUp(PointerUpEvent event) {
    _sketch.pointerUp(event);
    _setPainting(false);
  }

  void _pointerCancel(PointerCancelEvent event) {
    _sketch.pointerCancel(event);
    _setPainting(false);
  }

  void _undo() {
    setState(_sketch.undo);
  }

  void _clear() {
    setState(() {
      _sketch.clear();
      _clearEffectTick++;
    });
  }

  Future<Uint8List?> _capturePng() async {
    final boundary =
        _canvasKey.currentContext?.findRenderObject() as RenderRepaintBoundary?;
    if (boundary == null) {
      return null;
    }
    final image = await boundary.toImage(pixelRatio: 2);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.png);
    return byteData?.buffer.asUint8List();
  }

  Future<void> _saveImage() async {
    final bytes = await _capturePng();
    if (bytes == null || !mounted) {
      return;
    }
    final directory = await getApplicationDocumentsDirectory();
    final file = File(
      '${directory.path}/zen-${DateTime.now().millisecondsSinceEpoch}.png',
    );
    await file.writeAsBytes(bytes);
    if (mounted) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Saved ${file.path}')));
    }
  }

  Future<void> _shareImage() async {
    final bytes = await _capturePng();
    if (bytes == null) {
      return;
    }
    final directory = await getTemporaryDirectory();
    final file = File('${directory.path}/zen.png');
    await file.writeAsBytes(bytes);
    await SharePlus.instance.share(
      ShareParams(
        files: [XFile(file.path, mimeType: 'image/png')],
        text: 'Zen',
      ),
    );
  }

  void _openBrushOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: ZenColors.paper,
      builder: (context) => OptionsSheet(
        title: 'Brush',
        children: [
          OptionRow(
            label: 'Dark',
            icon: const ColorDot(color: ZenColors.darkBrush),
            selected: _sketch.brushColor == BrushColor.dark,
            onTap: () {
              Navigator.pop(context);
              _setBrushColor(BrushColor.dark);
            },
          ),
          OptionRow(
            label: 'Amber',
            icon: const ColorDot(color: ZenColors.amberBrush),
            selected: _sketch.brushColor == BrushColor.amber,
            onTap: () {
              Navigator.pop(context);
              _setBrushColor(BrushColor.amber);
            },
          ),
          OptionRow(
            label: 'Blue',
            icon: const ColorDot(color: ZenColors.primary),
            selected: _sketch.brushColor == BrushColor.primary,
            onTap: () {
              Navigator.pop(context);
              _setBrushColor(BrushColor.primary);
            },
          ),
          OptionRow(
            label: 'Red',
            icon: const ColorDot(color: ZenColors.accent),
            selected: _sketch.brushColor == BrushColor.accent,
            onTap: () {
              Navigator.pop(context);
              _setBrushColor(BrushColor.accent);
            },
          ),
          OptionRow(
            label: 'Erase',
            icon: Image.asset('assets/images/eraser.png', width: 40),
            selected: _sketch.brushColor == BrushColor.erase,
            onTap: () {
              Navigator.pop(context);
              _setBrushColor(BrushColor.erase);
            },
          ),
          const Divider(height: 1),
          for (final size in BrushSize.values)
            OptionRow(
              label: size.label,
              icon: BrushSizePreview(size: size),
              selected: _sketch.brushSize == size,
              onTap: () {
                Navigator.pop(context);
                _setBrushSize(size);
              },
            ),
        ],
      ),
    );
  }

  void _openFlowerOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: ZenColors.paper,
      builder: (context) => OptionsSheet(
        title: 'Flowers',
        children: [
          for (final flower in Flower.values)
            OptionRow(
              label: flower.label,
              icon: flower.previewAsset == null
                  ? Image.asset('assets/images/none.png', width: 40)
                  : Image.asset(flower.previewAsset!, width: 40),
              selected: _sketch.flower == flower,
              onTap: () {
                Navigator.pop(context);
                _setFlower(flower);
              },
            ),
        ],
      ),
    );
  }

  void _openMusicOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: ZenColors.paper,
      builder: (context) => OptionsSheet(
        title: 'Music',
        children: [
          for (final track in ZenTrack.values)
            OptionRow(
              label: track.label,
              icon: Icon(
                track == _track
                    ? Icons.music_note_rounded
                    : Icons.library_music_rounded,
                color: ZenColors.darkBrush,
                size: 34,
              ),
              selected: _track == track,
              onTap: () {
                Navigator.pop(context);
                _setTrack(track);
              },
            ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ZenColors.paper,
      body: Stack(
        fit: StackFit.expand,
        children: [
          if (_ready)
            RepaintBoundary(
              key: _canvasKey,
              child: Listener(
                onPointerDown: _pointerDown,
                onPointerMove: _pointerMove,
                onPointerUp: _pointerUp,
                onPointerCancel: _pointerCancel,
                child: CustomPaint(
                  painter: ZenPainter(_sketch),
                  child: const SizedBox.expand(),
                ),
              ),
            )
          else
            const ColoredBox(color: ZenColors.paper),
          Positioned(
            right: 18,
            bottom: 18,
            child: ZenMenu(
              open: _menuOpen,
              musicEnabled: _musicEnabled,
              canUndo: _sketch.canUndo,
              onToggle: () => setState(() => _menuOpen = !_menuOpen),
              onUndo: _undo,
              onClear: _clear,
              onBrush: _openBrushOptions,
              onFlowers: _openFlowerOptions,
              onMusic: _toggleMusic,
              onTracks: _openMusicOptions,
              onSave: _saveImage,
              onShare: _shareImage,
            ),
          ),
          IgnorePointer(child: ClearWashOverlay(trigger: _clearEffectTick)),
        ],
      ),
    );
  }
}

class ZenSketchModel extends ChangeNotifier {
  final math.Random _random = math.Random();
  final List<DrawCommand> _commands = [];
  final List<Branch> _branches = [];
  final List<SketchSnapshot> _undoStack = [];
  ZenAssets? assets;
  BrushColor brushColor = BrushColor.dark;
  BrushSize brushSize = BrushSize.medium;
  Flower flower = Flower.poppy;
  final List<Offset> _strokePoints = [];
  Offset _lastInputPoint = Offset.zero;
  Offset _lastRenderedPoint = Offset.zero;
  double _lastFingerVelocity = 0;
  bool _touching = false;
  bool _reachedRadiusAfterReset = false;
  double _radius = SketchMetrics.absoluteMinRadius;
  double _strokeWidth = 1;
  int _step = 0;

  Iterable<DrawCommand> get commands => _commands;

  bool get canUndo => _undoStack.isNotEmpty;

  void pointerDown(PointerDownEvent event) {
    _recordSnapshot();
    _touching = true;
    _strokePoints
      ..clear()
      ..add(event.localPosition);
    _lastInputPoint = event.localPosition;
    _lastRenderedPoint = event.localPosition;
    _lastFingerVelocity = 0;
    _resetRadius();
    _strokeWidth = _targetStrokeWidth();
  }

  void pointerMove(PointerMoveEvent event) {
    if (!_touching) {
      return;
    }
    _addStrokePoint(event.localPosition);
    _updateRadius();
    notifyListeners();
  }

  void pointerUp(PointerUpEvent event) {
    if (_touching) {
      _addStrokePoint(event.localPosition);
      _flushStrokeTail();
    }
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

  void _addStrokePoint(Offset point) {
    final movement = point - _lastInputPoint;
    if (movement.distance < SketchMetrics.inputMinDistance) {
      return;
    }
    _lastFingerVelocity = movement.distance;
    _lastInputPoint = point;
    _strokePoints.add(point);
    if (_strokePoints.length >= 4) {
      _renderSplineSegment(
        _strokePoints[0],
        _strokePoints[1],
        _strokePoints[2],
        _strokePoints[3],
      );
      _strokePoints.removeAt(0);
    }
  }

  void _flushStrokeTail() {
    while (_strokePoints.length >= 2) {
      final p0 = _strokePoints.length > 2
          ? _strokePoints[0]
          : _strokePoints.first;
      final p1 = _strokePoints.length > 2
          ? _strokePoints[1]
          : _strokePoints.first;
      final p2 = _strokePoints.length > 2
          ? _strokePoints[2]
          : _strokePoints.last;
      final p3 = _strokePoints.length > 3 ? _strokePoints[3] : p2;
      _renderSplineSegment(p0, p1, p2, p3);
      _strokePoints.removeAt(0);
      if (_strokePoints.length == 1) {
        _strokePoints.clear();
      }
    }
  }

  void _renderSplineSegment(Offset p0, Offset p1, Offset p2, Offset p3) {
    final control1 = p1 + (p2 - p0) / 6;
    final control2 = p2 - (p3 - p1) / 6;
    final desiredWidth = _targetStrokeWidth();
    _strokeWidth +=
        (desiredWidth - _strokeWidth) * SketchMetrics.widthSmoothing;
    final color = brushColor.color;
    _commands.add(
      CubicCommand(
        _lastRenderedPoint,
        control1,
        control2,
        p2,
        color,
        _strokeWidth,
        1,
      ),
    );
    _paintInkTexture(p2, _strokeWidth);
    _lastRenderedPoint = p2;
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
        _random.nextInt(100) > 94) {
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
    if (brushColor != BrushColor.erase && _random.nextInt(100) > 82) {
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
        _lastFingerVelocity < SketchMetrics.velocityThreshold) {
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
      _bloomFrom(Branch.createAt(_lastRenderedPoint + offset, _random));
    }
  }

  void _bloomFrom(Branch branch) {
    if (_branches.length < 1000 && branch.canBloom && flower != Flower.none) {
      _branches.add(Branch.createFrom(branch, _random));
    }
  }

  double get _fingerVelocity => _lastFingerVelocity;

  bool _isFingerMoving() => _lastFingerVelocity > 0.1;

  int get _horizontalDirection =>
      (_lastInputPoint.dx - _lastRenderedPoint.dx) >= 0 ? 1 : -1;

  int get _verticalDirection =>
      (_lastInputPoint.dy - _lastRenderedPoint.dy) >= 0 ? 1 : -1;

  double _randomInRange(double min, double max) {
    return min + _random.nextDouble() * (max - min);
  }
}

class ZenPainter extends CustomPainter {
  ZenPainter(this.model) : super(repaint: model);

  final ZenSketchModel model;

  @override
  void paint(Canvas canvas, Size size) {
    canvas.drawColor(ZenColors.paper, BlendMode.src);
    for (final command in model.commands) {
      command.draw(canvas);
    }
  }

  @override
  bool shouldRepaint(covariant ZenPainter oldDelegate) =>
      oldDelegate.model != model;
}

class ClearWashOverlay extends StatelessWidget {
  const ClearWashOverlay({required this.trigger, super.key});

  final int trigger;

  @override
  Widget build(BuildContext context) {
    if (trigger == 0) {
      return const SizedBox.expand();
    }
    return TweenAnimationBuilder<double>(
      key: ValueKey(trigger),
      tween: Tween(begin: 0, end: 1),
      duration: const Duration(milliseconds: 620),
      curve: Curves.easeOutCubic,
      builder: (context, progress, _) {
        return CustomPaint(
          painter: ClearWashPainter(progress),
          child: const SizedBox.expand(),
        );
      },
    );
  }
}

class ClearWashPainter extends CustomPainter {
  ClearWashPainter(this.progress);

  final double progress;

  @override
  void paint(Canvas canvas, Size size) {
    final origin = Offset(size.width - 64, size.height - 44);
    final maxRadius = math.sqrt(
      size.width * size.width + size.height * size.height,
    );
    final radius = maxRadius * progress;
    final fade = (1 - progress).clamp(0.0, 1.0);

    canvas.drawCircle(
      origin,
      radius,
      Paint()
        ..color = ZenColors.paper.withValues(alpha: 0.72 * fade)
        ..style = PaintingStyle.fill,
    );

    canvas.drawCircle(
      origin,
      radius,
      Paint()
        ..color = ZenColors.darkBrush.withValues(alpha: 0.10 * fade)
        ..style = PaintingStyle.stroke
        ..strokeWidth = 1.2 + 3 * fade
        ..maskFilter = const MaskFilter.blur(BlurStyle.normal, 2),
    );

    final sweep = Rect.fromLTWH(
      0,
      size.height * (1 - progress),
      size.width,
      size.height * 0.22,
    );
    canvas.drawRect(
      sweep,
      Paint()
        ..shader = LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            ZenColors.paper.withValues(alpha: 0),
            Colors.white.withValues(alpha: 0.22 * fade),
            ZenColors.paper.withValues(alpha: 0),
          ],
        ).createShader(sweep),
    );
  }

  @override
  bool shouldRepaint(covariant ClearWashPainter oldDelegate) {
    return oldDelegate.progress != progress;
  }
}

abstract class DrawCommand {
  void draw(Canvas canvas);
}

class LineCommand implements DrawCommand {
  LineCommand(this.start, this.end, this.color, this.width, this.opacity);

  final Offset start;
  final Offset end;
  final Color color;
  final double width;
  final double opacity;

  @override
  void draw(Canvas canvas) {
    canvas.drawLine(
      start,
      end,
      Paint()
        ..color = color.withValues(alpha: opacity)
        ..strokeCap = StrokeCap.round
        ..strokeWidth = width
        ..isAntiAlias = true,
    );
  }
}

class CurveCommand implements DrawCommand {
  CurveCommand(
    this.start,
    this.control,
    this.end,
    this.color,
    this.width,
    this.opacity,
  );

  final Offset start;
  final Offset control;
  final Offset end;
  final Color color;
  final double width;
  final double opacity;

  @override
  void draw(Canvas canvas) {
    final path = Path()
      ..moveTo(start.dx, start.dy)
      ..quadraticBezierTo(control.dx, control.dy, end.dx, end.dy);
    canvas.drawPath(
      path,
      Paint()
        ..color = color.withValues(alpha: opacity)
        ..style = PaintingStyle.stroke
        ..strokeCap = StrokeCap.round
        ..strokeJoin = StrokeJoin.round
        ..strokeWidth = width
        ..isAntiAlias = true,
    );
  }
}

class CubicCommand implements DrawCommand {
  CubicCommand(
    this.start,
    this.control1,
    this.control2,
    this.end,
    this.color,
    this.width,
    this.opacity,
  );

  final Offset start;
  final Offset control1;
  final Offset control2;
  final Offset end;
  final Color color;
  final double width;
  final double opacity;

  @override
  void draw(Canvas canvas) {
    final path = Path()
      ..moveTo(start.dx, start.dy)
      ..cubicTo(
        control1.dx,
        control1.dy,
        control2.dx,
        control2.dy,
        end.dx,
        end.dy,
      );
    canvas.drawPath(
      path,
      Paint()
        ..color = color.withValues(alpha: opacity)
        ..style = PaintingStyle.stroke
        ..strokeCap = StrokeCap.round
        ..strokeJoin = StrokeJoin.round
        ..strokeWidth = width
        ..isAntiAlias = true,
    );
  }
}

class PointCommand implements DrawCommand {
  PointCommand(this.point, this.width, this.color, this.opacity);

  final Offset point;
  final double width;
  final Color color;
  final double opacity;

  @override
  void draw(Canvas canvas) {
    canvas.drawCircle(
      point,
      width / 2,
      Paint()
        ..color = color.withValues(alpha: opacity)
        ..isAntiAlias = true,
    );
  }
}

class InkBleedCommand implements DrawCommand {
  InkBleedCommand({
    required this.center,
    required this.radius,
    required this.color,
    required this.opacity,
  });

  final Offset center;
  final double radius;
  final Color color;
  final double opacity;

  @override
  void draw(Canvas canvas) {
    final rect = Rect.fromCircle(center: center, radius: radius);
    canvas.drawOval(
      rect,
      Paint()
        ..color = color.withValues(alpha: opacity)
        ..maskFilter = MaskFilter.blur(BlurStyle.normal, radius * 0.18)
        ..isAntiAlias = true,
    );
  }
}

class ImageCommand implements DrawCommand {
  ImageCommand({
    required this.image,
    required this.center,
    required this.size,
    required this.rotation,
    required this.tint,
    required this.flipX,
  });

  final ui.Image image;
  final Offset center;
  final Size size;
  final double rotation;
  final Color tint;
  final bool flipX;

  @override
  void draw(Canvas canvas) {
    final source = Rect.fromLTWH(
      0,
      0,
      image.width.toDouble(),
      image.height.toDouble(),
    );
    final destination = Rect.fromCenter(
      center: Offset.zero,
      width: size.width,
      height: size.height,
    );
    canvas.save();
    canvas.translate(center.dx, center.dy);
    canvas.rotate(rotation);
    if (flipX) {
      canvas.scale(-1, 1);
    }
    canvas.drawImageRect(
      image,
      source,
      destination,
      Paint()
        ..isAntiAlias = true
        ..filterQuality = FilterQuality.high
        ..colorFilter = ColorFilter.mode(tint, BlendMode.modulate),
    );
    canvas.restore();
  }
}

class Branch {
  Branch({
    required this.position,
    required this.angle,
    required this.radius,
    required this.step,
    required this.shrink,
  }) : previousPosition = position;

  Offset position;
  Offset previousPosition;
  double angle;
  double radius;
  double step;
  double shrink;

  bool get isDead => radius.abs() < SketchMetrics.branchMinRadius;

  bool get canBloom => radius.abs() > SketchMetrics.branchMinBloomRadius;

  Branch clone() {
    return Branch(
      position: position,
      angle: angle,
      radius: radius,
      step: step,
      shrink: shrink,
    )..previousPosition = previousPosition;
  }

  void update() {
    previousPosition = position;
    position += Offset(radius * math.cos(angle), radius * math.sin(angle));
    angle += step;
    radius *= shrink;
  }

  static Branch createAt(Offset position, math.Random random) {
    return Branch(
      position: position,
      angle: _randomInRange(random, -math.pi, math.pi),
      radius: SketchMetrics.branchDefaultRadius,
      step: 0,
      shrink: _randomInRange(random, 0.94, 0.96),
    );
  }

  static Branch createFrom(Branch branch, math.Random random) {
    final randomStep = _randomInRange(random, 0.05, 0.25);
    return Branch(
      position: branch.position,
      angle: branch.angle,
      radius: branch.radius * _randomInRange(random, 0.9, 1.1),
      step: branch.step >= 0 ? -randomStep : randomStep,
      shrink: _randomInRange(random, 0.94, 0.96),
    );
  }

  static double _randomInRange(math.Random random, double min, double max) {
    return min + random.nextDouble() * (max - min);
  }
}

class SketchSnapshot {
  SketchSnapshot(this.commands, this.branches);

  final List<DrawCommand> commands;
  final List<Branch> branches;
}

extension OffsetVector on Offset {
  Offset get normalizedOrZero {
    final length = distance;
    if (length == 0) {
      return Offset.zero;
    }
    return this / length;
  }
}

class ZenAssets {
  ZenAssets({
    required this.brushInk,
    required this.birdsfoot,
    required this.poppy,
    required this.meconopsis,
    required this.cherry,
    required this.blueDahlia,
    required this.addisonia,
  });

  final ui.Image? brushInk;
  final List<ui.Image> birdsfoot;
  final List<ui.Image> poppy;
  final List<ui.Image> meconopsis;
  final List<ui.Image> cherry;
  final List<ui.Image> blueDahlia;
  final List<ui.Image> addisonia;

  static Future<ZenAssets> load() async {
    return ZenAssets(
      brushInk: await _loadImage('assets/images/brush_ink.png'),
      birdsfoot: await _loadImages('birdsfoot'),
      poppy: await _loadImages('poppy'),
      meconopsis: await _loadImages('meconopsis'),
      cherry: await _loadImages('cherry'),
      blueDahlia: await _loadSingleImage('blue_flower_commons'),
      addisonia: await _loadSingleImage('addisonia_commons'),
    );
  }

  List<ui.Image> flowerImages(Flower flower) {
    return switch (flower) {
      Flower.none => const <ui.Image>[],
      Flower.birdsfoot => birdsfoot,
      Flower.poppy => poppy,
      Flower.meconopsis => meconopsis,
      Flower.cherry => cherry,
      Flower.blueDahlia => blueDahlia,
      Flower.addisonia => addisonia,
    };
  }

  static Future<List<ui.Image>> _loadSingleImage(String name) async {
    final image = await _loadImage('assets/images/$name.png');
    return image == null ? const <ui.Image>[] : [image];
  }

  static Future<List<ui.Image>> _loadImages(String name) async {
    final images = <ui.Image>[];
    for (var i = 1; i <= 5; i++) {
      final image = await _loadImage('assets/images/${name}_$i.png');
      if (image != null) {
        images.add(image);
      }
    }
    return images;
  }

  static Future<ui.Image?> _loadImage(String asset) async {
    try {
      final data = await rootBundle.load(asset);
      final codec = await ui.instantiateImageCodec(data.buffer.asUint8List());
      final frame = await codec.getNextFrame();
      return frame.image;
    } on FlutterError {
      return null;
    }
  }
}

class ZenMenu extends StatelessWidget {
  const ZenMenu({
    required this.open,
    required this.musicEnabled,
    required this.canUndo,
    required this.onToggle,
    required this.onUndo,
    required this.onClear,
    required this.onBrush,
    required this.onFlowers,
    required this.onMusic,
    required this.onTracks,
    required this.onSave,
    required this.onShare,
    super.key,
  });

  final bool open;
  final bool musicEnabled;
  final bool canUndo;
  final VoidCallback onToggle;
  final VoidCallback onUndo;
  final VoidCallback onClear;
  final VoidCallback onBrush;
  final VoidCallback onFlowers;
  final VoidCallback onMusic;
  final VoidCallback onTracks;
  final VoidCallback onSave;
  final VoidCallback onShare;

  @override
  Widget build(BuildContext context) {
    final primaryActions = [
      MenuAction(Icons.undo_rounded, 'Undo', onUndo, disabled: !canUndo),
      MenuAction(Icons.brush_rounded, 'Brush', onBrush),
      MenuAction(Icons.local_florist_rounded, 'Flowers', onFlowers),
      MenuAction(
        musicEnabled ? Icons.volume_up_rounded : Icons.volume_off_rounded,
        'Sound',
        onMusic,
        dimmed: !musicEnabled,
      ),
    ];
    final secondaryActions = [
      MenuAction(Icons.queue_music_rounded, 'Tracks', onTracks),
      MenuAction(Icons.ios_share_rounded, 'Share', onShare),
      MenuAction(Icons.download_rounded, 'Save', onSave),
      MenuAction(Icons.refresh_rounded, 'Clear', onClear),
    ];
    return Material(
      color: Colors.transparent,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 180),
        curve: Curves.easeOutCubic,
        width: open ? 356 : 306,
        decoration: BoxDecoration(
          color: ZenColors.panel,
          border: Border.all(color: ZenColors.panelBorder),
          borderRadius: BorderRadius.circular(8),
          boxShadow: const [
            BoxShadow(
              blurRadius: 18,
              offset: Offset(0, 8),
              color: Color(0x26000000),
            ),
          ],
        ),
        padding: const EdgeInsets.all(8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                MenuToggleButton(open: open, onTap: onToggle),
                const SizedBox(width: 6),
                for (final action in primaryActions) ...[
                  Expanded(child: MenuButton(action: action)),
                  const SizedBox(width: 6),
                ],
              ],
            ),
            AnimatedCrossFade(
              firstChild: const SizedBox.shrink(),
              secondChild: Padding(
                padding: const EdgeInsets.only(top: 8),
                child: Row(
                  children: [
                    for (final action in secondaryActions) ...[
                      Expanded(child: MenuButton(action: action)),
                      if (action != secondaryActions.last)
                        const SizedBox(width: 6),
                    ],
                  ],
                ),
              ),
              crossFadeState: open
                  ? CrossFadeState.showSecond
                  : CrossFadeState.showFirst,
              duration: const Duration(milliseconds: 160),
            ),
          ],
        ),
      ),
    );
  }
}

class MenuToggleButton extends StatelessWidget {
  const MenuToggleButton({required this.open, required this.onTap, super.key});

  final bool open;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(8),
      onTap: onTap,
      child: Container(
        width: 48,
        height: 50,
        decoration: BoxDecoration(
          color: ZenColors.darkBrush,
          borderRadius: BorderRadius.circular(8),
        ),
        alignment: Alignment.center,
        child: Icon(
          open ? Icons.close_rounded : Icons.tune_rounded,
          color: ZenColors.paper,
        ),
      ),
    );
  }
}

class MenuButton extends StatelessWidget {
  const MenuButton({required this.action, super.key});

  final MenuAction action;

  @override
  Widget build(BuildContext context) {
    final foreground = action.disabled
        ? ZenColors.darkBrush.withValues(alpha: 0.28)
        : action.dimmed
        ? ZenColors.darkBrush.withValues(alpha: 0.52)
        : ZenColors.darkBrush;
    return InkWell(
      borderRadius: BorderRadius.circular(8),
      onTap: action.disabled ? null : action.onTap,
      child: Container(
        height: 50,
        decoration: BoxDecoration(
          color: action.disabled
              ? Colors.transparent
              : ZenColors.paper.withValues(alpha: 0.74),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(action.icon, size: 20, color: foreground),
            const SizedBox(height: 2),
            Text(
              action.label,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(
                color: foreground,
                fontSize: 10,
                height: 1,
                letterSpacing: 0,
                fontWeight: FontWeight.w600,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class MenuAction {
  MenuAction(
    this.icon,
    this.label,
    this.onTap, {
    this.dimmed = false,
    this.disabled = false,
  });

  final IconData icon;
  final String label;
  final VoidCallback onTap;
  final bool dimmed;
  final bool disabled;
}

class OptionsSheet extends StatelessWidget {
  const OptionsSheet({required this.title, required this.children, super.key});

  final String title;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 400),
        child: ListView(
          shrinkWrap: true,
          padding: const EdgeInsets.symmetric(vertical: 14),
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 8),
              child: Text(title, style: Theme.of(context).textTheme.titleLarge),
            ),
            ...children,
          ],
        ),
      ),
    );
  }
}

class OptionRow extends StatelessWidget {
  const OptionRow({
    required this.label,
    required this.icon,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final String label;
  final Widget icon;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: SizedBox(width: 52, height: 52, child: Center(child: icon)),
      title: Text(label),
      trailing: selected ? const Icon(Icons.check) : null,
      onTap: onTap,
    );
  }
}

class ColorDot extends StatelessWidget {
  const ColorDot({required this.color, super.key});

  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 40,
      height: 40,
      decoration: BoxDecoration(color: color, shape: BoxShape.circle),
    );
  }
}

class BrushSizePreview extends StatelessWidget {
  const BrushSizePreview({required this.size, super.key});

  final BrushSize size;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        width: size.previewDiameter,
        height: size.previewDiameter,
        decoration: const BoxDecoration(
          color: ZenColors.darkBrush,
          shape: BoxShape.circle,
        ),
      ),
    );
  }
}

enum BrushColor {
  dark(ZenColors.darkBrush),
  amber(ZenColors.amberBrush),
  primary(ZenColors.primary),
  accent(ZenColors.accent),
  erase(ZenColors.paper);

  const BrushColor(this.color);

  final Color color;
}

enum BrushSize {
  small('Small', 54),
  medium('Medium', 84),
  large('Large', 120);

  const BrushSize(this.label, this.radius);

  final String label;
  final double radius;

  double get minRadius => math.max(0.75, radius * 0.18);

  double get previewDiameter => math.max(12, radius / 3);
}

enum Flower {
  none('None', null, 18, 25),
  birdsfoot('Birdsfoot', 'assets/images/birdsfoot.png', 18 * 1.2, 25 * 1.2),
  poppy('Poppy', 'assets/images/poppy.png', 18, 25),
  meconopsis('Meconopsis', 'assets/images/meconopsis.png', 18 * 0.8, 25 * 0.8),
  cherry('Cherry', 'assets/images/cherry.png', 18, 25),
  blueDahlia('Blue dahlia', 'assets/images/blue_flower_commons.png', 20, 32),
  addisonia('Addisonia', 'assets/images/addisonia_commons.png', 24, 38);

  const Flower(this.label, this.previewAsset, this.minSize, this.maxSize);

  final String label;
  final String? previewAsset;
  final double minSize;
  final double maxSize;
}

enum ZenTrack {
  original('Original Zen', 'assets/audio/zen.mp3'),
  reachingTheSky('Reaching The Sky', 'assets/audio/reaching_the_sky.m4a'),
  placidAmbient('Placid Ambient', 'assets/audio/placid_ambient.m4a');

  const ZenTrack(this.label, this.asset);

  final String label;
  final String asset;
}

class ZenColors {
  static const accent = Color(0xFFFF5252);
  static const primary = Color(0xFF3F51B5);
  static const paper = Color(0xFFFFFDE7);
  static const panel = Color(0xF8FFFFFF);
  static const panelBorder = Color(0x1F212121);
  static const darkBrush = Color(0xFF212121);
  static const amberBrush = Color(0xFFFFC107);
}

class SketchMetrics {
  static const absoluteMinRadius = 0.75;
  static const inkDropStep = 0.5;
  static const velocityThreshold = 16.0;
  static const inputMinDistance = 1.2;
  static const widthSmoothing = 0.35;
  static const restingMusicVolume = 0.05;
  static const paintingMusicVolume = 1.0;
  static const musicStep = 0.005;
  static const musicStepInterval = Duration(milliseconds: 16);
  static const branchMinRadius = 0.25;
  static const branchMinBloomRadius = 1.5;
  static const branchDefaultRadius = 3.0;
}

class Prefs {
  static const brushColor = 'brush_color';
  static const brushSize = 'brush_size';
  static const flower = 'flower';
  static const musicEnabled = 'music_enabled';
  static const track = 'track';
}
