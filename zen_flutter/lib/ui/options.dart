part of '../main.dart';

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
