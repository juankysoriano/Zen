part of '../main.dart';

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
      MenuAction(LucideIcons.undo2, 'Undo', onUndo, disabled: !canUndo),
      MenuAction(LucideIcons.brush, 'Brush', onBrush),
      MenuAction(LucideIcons.flower2, 'Flowers', onFlowers),
      MenuAction(
        musicEnabled ? LucideIcons.volume2 : LucideIcons.volumeX,
        'Sound',
        onMusic,
        dimmed: !musicEnabled,
      ),
    ];
    final secondaryActions = [
      MenuAction(LucideIcons.listMusic, 'Tracks', onTracks),
      MenuAction(LucideIcons.share2, 'Share', onShare),
      MenuAction(LucideIcons.download, 'Save', onSave),
      MenuAction(LucideIcons.trash2, 'Clear', onClear),
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
          open ? LucideIcons.x : LucideIcons.slidersHorizontal,
          color: ZenColors.paper,
          size: 23,
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
            Icon(action.icon, size: 21, color: foreground),
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
