import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:zen_flutter/main.dart';

void main() {
  testWidgets('menu renders primary control', (tester) async {
    await tester.pumpWidget(
      Directionality(
        textDirection: TextDirection.ltr,
        child: ZenMenu(
          open: false,
          musicEnabled: true,
          canUndo: false,
          onToggle: () {},
          onUndo: () {},
          onClear: () {},
          onBrush: () {},
          onFlowers: () {},
          onMusic: () {},
          onTracks: () {},
          onSave: () {},
          onShare: () {},
        ),
      ),
    );

    expect(find.byType(ZenMenu), findsOneWidget);
  });
}
