part of '../main.dart';

class OptionsSheet extends StatelessWidget {
  const OptionsSheet({required this.title, required this.children, super.key});

  final String title;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: false,
      child: Align(
        alignment: Alignment.bottomCenter,
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 430),
          child: Container(
            margin: const EdgeInsets.fromLTRB(10, 0, 10, 10),
            padding: const EdgeInsets.fromLTRB(14, 10, 14, 16),
            decoration: BoxDecoration(
              color: ZenColors.paper,
              border: Border.all(color: ZenColors.panelBorder),
              borderRadius: BorderRadius.circular(8),
              boxShadow: const [
                BoxShadow(
                  blurRadius: 22,
                  offset: Offset(0, 10),
                  color: Color(0x26000000),
                ),
              ],
            ),
            child: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Center(
                    child: Container(
                      width: 38,
                      height: 4,
                      decoration: BoxDecoration(
                        color: ZenColors.darkBrush.withValues(alpha: 0.18),
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 4),
                    child: Text(
                      title,
                      style: Theme.of(context).textTheme.titleLarge?.copyWith(
                        color: ZenColors.darkBrush,
                        fontWeight: FontWeight.w700,
                        letterSpacing: 0,
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  ...children,
                ],
              ),
            ),
          ),
        ),
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
      padding: const EdgeInsets.only(bottom: 14),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(4, 0, 4, 8),
            child: Text(
              label,
              style: Theme.of(context).textTheme.labelLarge?.copyWith(
                color: ZenColors.darkBrush.withValues(alpha: 0.62),
                fontWeight: FontWeight.w700,
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

class OptionGrid extends StatelessWidget {
  const OptionGrid({required this.children, this.columns = 3, super.key});

  final List<Widget> children;
  final int columns;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        const spacing = 8.0;
        final tileWidth =
            (constraints.maxWidth - spacing * (columns - 1)) / columns;
        return Wrap(
          spacing: spacing,
          runSpacing: spacing,
          children: [
            for (final child in children)
              SizedBox(width: tileWidth, child: child),
          ],
        );
      },
    );
  }
}

class ChoiceTile extends StatelessWidget {
  const ChoiceTile({
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
    final borderColor = selected
        ? ZenColors.darkBrush
        : ZenColors.panelBorder.withValues(alpha: 0.8);
    return InkWell(
      borderRadius: BorderRadius.circular(8),
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 140),
        curve: Curves.easeOutCubic,
        height: 96,
        padding: const EdgeInsets.fromLTRB(8, 8, 8, 7),
        decoration: BoxDecoration(
          color: selected
              ? Colors.white.withValues(alpha: 0.82)
              : Colors.white.withValues(alpha: 0.48),
          border: Border.all(color: borderColor, width: selected ? 1.4 : 1),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Stack(
          children: [
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Expanded(child: Center(child: preview)),
                const SizedBox(height: 6),
                Text(
                  label,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: ZenColors.darkBrush.withValues(alpha: 0.86),
                    fontSize: 12,
                    height: 1,
                    letterSpacing: 0,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ],
            ),
            if (selected)
              Align(
                alignment: Alignment.topRight,
                child: Icon(
                  LucideIcons.check,
                  size: 16,
                  color: ZenColors.darkBrush.withValues(alpha: 0.74),
                ),
              ),
          ],
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
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: InkWell(
        borderRadius: BorderRadius.circular(8),
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 140),
          curve: Curves.easeOutCubic,
          constraints: const BoxConstraints(minHeight: 62),
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
          decoration: BoxDecoration(
            color: selected
                ? Colors.white.withValues(alpha: 0.82)
                : Colors.white.withValues(alpha: 0.48),
            border: Border.all(
              color: selected
                  ? ZenColors.darkBrush
                  : ZenColors.panelBorder.withValues(alpha: 0.8),
              width: selected ? 1.4 : 1,
            ),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Row(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: ZenColors.darkBrush.withValues(alpha: 0.08),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Icon(
                  LucideIcons.music,
                  color: ZenColors.darkBrush,
                  size: 21,
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  track.label,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(
                    color: ZenColors.darkBrush,
                    fontSize: 15,
                    height: 1.1,
                    letterSpacing: 0,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ),
              if (selected)
                const Icon(
                  LucideIcons.check,
                  color: ZenColors.darkBrush,
                  size: 18,
                ),
            ],
          ),
        ),
      ),
    );
  }
}

class ColorDot extends StatelessWidget {
  const ColorDot({required this.color, super.key});

  final Color color;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 42,
      height: 42,
      decoration: BoxDecoration(
        color: color,
        shape: BoxShape.circle,
        border: Border.all(
          color: color == ZenColors.paper
              ? ZenColors.darkBrush.withValues(alpha: 0.28)
              : Colors.transparent,
        ),
      ),
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

class FlowerPreview extends StatelessWidget {
  const FlowerPreview({required this.flower, super.key});

  final Flower flower;

  @override
  Widget build(BuildContext context) {
    if (flower.previewAsset == null) {
      return const Icon(
        LucideIcons.flower,
        color: ZenColors.darkBrush,
        size: 30,
      );
    }
    return Image.asset(
      flower.previewAsset!,
      width: 46,
      height: 46,
      fit: BoxFit.contain,
    );
  }
}
