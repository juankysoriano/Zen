part of '../main.dart';

class OptionsSheet extends StatelessWidget {
  const OptionsSheet({required this.title, required this.children, super.key});

  final String title;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.sizeOf(context);
    final isWide = size.width > size.height;
    return SafeArea(
      top: false,
      child: Align(
        alignment: isWide ? Alignment.centerRight : Alignment.bottomCenter,
        child: ConstrainedBox(
          constraints: BoxConstraints(
            maxWidth: isWide ? 420 : size.width - 20,
            maxHeight: isWide ? size.height - 28 : size.height * 0.74,
          ),
          child: Container(
            margin: EdgeInsets.fromLTRB(10, 10, isWide ? 16 : 10, 12),
            decoration: BoxDecoration(
              color: const Color(0xFFFFFBEA),
              border: Border.all(color: const Color(0x24212121)),
              borderRadius: BorderRadius.circular(8),
              boxShadow: const [
                BoxShadow(
                  blurRadius: 24,
                  offset: Offset(0, 10),
                  color: Color(0x30000000),
                ),
              ],
            ),
            child: ClipRRect(
              borderRadius: BorderRadius.circular(8),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  _DrawerHeader(title: title),
                  Flexible(
                    child: SingleChildScrollView(
                      padding: const EdgeInsets.fromLTRB(14, 12, 14, 14),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: children,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class _DrawerHeader extends StatelessWidget {
  const _DrawerHeader({required this.title});

  final String title;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(16, 14, 12, 12),
      decoration: const BoxDecoration(
        border: Border(bottom: BorderSide(color: Color(0x16212121))),
      ),
      child: Row(
        children: [
          Expanded(
            child: Text(
              title,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: Theme.of(context).textTheme.titleLarge?.copyWith(
                color: ZenColors.darkBrush,
                fontWeight: FontWeight.w700,
                letterSpacing: 0,
                height: 1,
              ),
            ),
          ),
          Container(
            width: 34,
            height: 34,
            decoration: BoxDecoration(
              color: ZenColors.darkBrush.withValues(alpha: 0.06),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Icon(
              LucideIcons.chevronDown,
              color: ZenColors.darkBrush.withValues(alpha: 0.66),
              size: 19,
            ),
          ),
        ],
      ),
    );
  }
}

class OptionsSection extends StatelessWidget {
  const OptionsSection({required this.label, required this.child, super.key});

  final String label;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(2, 0, 2, 8),
            child: Text(
              label,
              style: Theme.of(context).textTheme.labelMedium?.copyWith(
                color: ZenColors.darkBrush.withValues(alpha: 0.52),
                fontWeight: FontWeight.w800,
                letterSpacing: 0,
              ),
            ),
          ),
          child,
        ],
      ),
    );
  }
}

class HorizontalOptionStrip extends StatelessWidget {
  const HorizontalOptionStrip({required this.children, super.key});

  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      clipBehavior: Clip.none,
      child: Row(
        children: [
          for (var index = 0; index < children.length; index++) ...[
            children[index],
            if (index != children.length - 1) const SizedBox(width: 10),
          ],
        ],
      ),
    );
  }
}

class InkChoice extends StatelessWidget {
  const InkChoice({
    required this.label,
    required this.color,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final String label;
  final BrushColor color;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final isErase = color == BrushColor.erase;
    return InkWell(
      borderRadius: BorderRadius.circular(8),
      onTap: onTap,
      child: SizedBox(
        width: 62,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            AnimatedContainer(
              duration: const Duration(milliseconds: 160),
              curve: Curves.easeOutCubic,
              width: 48,
              height: 48,
              decoration: BoxDecoration(
                color: isErase ? Colors.transparent : color.color,
                shape: BoxShape.circle,
                border: Border.all(
                  color: selected
                      ? ZenColors.darkBrush
                      : ZenColors.darkBrush.withValues(alpha: 0.18),
                  width: selected ? 2 : 1,
                ),
                boxShadow: selected
                    ? const [
                        BoxShadow(
                          blurRadius: 10,
                          offset: Offset(0, 4),
                          color: Color(0x22000000),
                        ),
                      ]
                    : null,
              ),
              child: isErase
                  ? const Icon(
                      LucideIcons.eraser,
                      color: ZenColors.darkBrush,
                      size: 22,
                    )
                  : null,
            ),
            const SizedBox(height: 7),
            Text(
              label,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: ZenColors.darkBrush.withValues(
                  alpha: selected ? 0.88 : 0.52,
                ),
                fontSize: 11,
                height: 1,
                letterSpacing: 0,
                fontWeight: selected ? FontWeight.w800 : FontWeight.w600,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class WeightChoice extends StatelessWidget {
  const WeightChoice({
    required this.label,
    required this.size,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final String label;
  final BrushSize size;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return PreviewRow(
      label: label,
      selected: selected,
      onTap: onTap,
      preview: BrushStrokePreview(size: size),
    );
  }
}

class FlowerChoice extends StatelessWidget {
  const FlowerChoice({
    required this.flower,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final Flower flower;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return PreviewRow(
      label: flower.label,
      selected: selected,
      onTap: onTap,
      preview: FlowerPreview(flower: flower),
    );
  }
}

class PreviewRow extends StatelessWidget {
  const PreviewRow({
    required this.label,
    required this.preview,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final String label;
  final Widget preview;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 6),
      child: InkWell(
        borderRadius: BorderRadius.circular(8),
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 140),
          curve: Curves.easeOutCubic,
          constraints: const BoxConstraints(minHeight: 54),
          padding: const EdgeInsets.fromLTRB(10, 8, 10, 8),
          decoration: BoxDecoration(
            color: selected
                ? ZenColors.darkBrush.withValues(alpha: 0.055)
                : Colors.transparent,
            border: Border.all(
              color: selected
                  ? ZenColors.darkBrush.withValues(alpha: 0.34)
                  : Colors.transparent,
            ),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Row(
            children: [
              SizedBox(width: 96, child: preview),
              const SizedBox(width: 10),
              Expanded(
                child: Text(
                  label,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    color: ZenColors.darkBrush.withValues(
                      alpha: selected ? 0.92 : 0.68,
                    ),
                    fontSize: 15,
                    height: 1,
                    letterSpacing: 0,
                    fontWeight: selected ? FontWeight.w800 : FontWeight.w600,
                  ),
                ),
              ),
              AnimatedOpacity(
                duration: const Duration(milliseconds: 140),
                opacity: selected ? 1 : 0,
                child: const Icon(
                  LucideIcons.check,
                  color: ZenColors.darkBrush,
                  size: 18,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class TrackChoiceTile extends StatelessWidget {
  const TrackChoiceTile({
    required this.track,
    required this.selected,
    required this.onTap,
    super.key,
  });

  final ZenTrack track;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return PreviewRow(
      label: track.label,
      selected: selected,
      onTap: onTap,
      preview: const TrackWavePreview(),
    );
  }
}

class BrushStrokePreview extends StatelessWidget {
  const BrushStrokePreview({required this.size, super.key});

  final BrushSize size;

  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      size: const Size(92, 28),
      painter: _StrokePreviewPainter(size.radius / 16),
    );
  }
}

class TrackWavePreview extends StatelessWidget {
  const TrackWavePreview({super.key});

  @override
  Widget build(BuildContext context) {
    return CustomPaint(size: const Size(92, 28), painter: _TrackWavePainter());
  }
}

class FlowerPreview extends StatelessWidget {
  const FlowerPreview({required this.flower, super.key});

  final Flower flower;

  @override
  Widget build(BuildContext context) {
    if (flower.previewAsset == null) {
      return Icon(
        LucideIcons.flower,
        color: ZenColors.darkBrush.withValues(alpha: 0.72),
        size: 28,
      );
    }
    return Align(
      alignment: Alignment.centerLeft,
      child: Image.asset(
        flower.previewAsset!,
        width: 46,
        height: 46,
        fit: BoxFit.contain,
      ),
    );
  }
}

class _StrokePreviewPainter extends CustomPainter {
  const _StrokePreviewPainter(this.weight);

  final double weight;

  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = ZenColors.darkBrush.withValues(alpha: 0.84)
      ..strokeCap = StrokeCap.round
      ..strokeWidth = weight.clamp(3, 9).toDouble()
      ..style = PaintingStyle.stroke;
    final path = Path()
      ..moveTo(4, size.height * 0.52)
      ..cubicTo(
        size.width * 0.26,
        size.height * 0.18,
        size.width * 0.54,
        size.height * 0.86,
        size.width - 4,
        size.height * 0.44,
      );
    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(_StrokePreviewPainter oldDelegate) {
    return oldDelegate.weight != weight;
  }
}

class _TrackWavePainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()
      ..color = ZenColors.darkBrush.withValues(alpha: 0.72)
      ..strokeCap = StrokeCap.round
      ..strokeWidth = 2;
    final heights = [8.0, 16.0, 11.0, 22.0, 13.0, 18.0, 9.0];
    final gap = size.width / (heights.length + 1);
    for (var i = 0; i < heights.length; i++) {
      final x = gap * (i + 1);
      final half = heights[i] / 2;
      canvas.drawLine(
        Offset(x, size.height / 2 - half),
        Offset(x, size.height / 2 + half),
        paint,
      );
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
