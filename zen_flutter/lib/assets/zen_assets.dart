part of '../main.dart';

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
