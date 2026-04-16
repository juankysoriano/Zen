import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:zen_flutter/main.dart';

void main() {
  test('pointer drag paints ink commands', () {
    final model = ZenSketchModel();

    model.pointerDown(const PointerDownEvent(position: Offset(12, 12)));
    model.pointerMove(const PointerMoveEvent(position: Offset(120, 80)));
    model.pointerUp(const PointerUpEvent(position: Offset(120, 80)));

    expect(model.commands, isNotEmpty);
  });
}
