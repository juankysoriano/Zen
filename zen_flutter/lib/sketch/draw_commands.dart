part of '../main.dart';

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
