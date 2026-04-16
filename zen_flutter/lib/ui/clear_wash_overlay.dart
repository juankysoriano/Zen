part of '../main.dart';

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
