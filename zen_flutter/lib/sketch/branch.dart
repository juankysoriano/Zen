part of '../main.dart';

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

class FingerPositionSmoother {
  static const _spring = 0.1;
  static const _damp = 0.6;

  Offset position = Offset.zero;
  Offset oldPosition = Offset.zero;
  Offset _velocity = Offset.zero;

  void resetTo(Offset point) {
    position = point;
    oldPosition = point;
    _velocity = Offset.zero;
  }

  void moveTo(Offset point) {
    oldPosition = position;
    final distance = (point - position) * _spring;
    _velocity = (_velocity + distance) * _damp;
    position += _velocity;
  }

  double get fingerVelocity => _velocity.distance;
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
