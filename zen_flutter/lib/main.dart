import 'dart:async';
import 'dart:io';
import 'dart:math' as math;
import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:just_audio/just_audio.dart';
import 'package:lucide_icons_flutter/lucide_icons.dart';
import 'package:path_provider/path_provider.dart';
import 'package:share_plus/share_plus.dart';
import 'package:shared_preferences/shared_preferences.dart';

part 'sketch/zen_sketch_model.dart';
part 'sketch/draw_commands.dart';
part 'sketch/branch.dart';
part 'assets/zen_assets.dart';
part 'ui/zen_menu.dart';
part 'ui/options.dart';
part 'ui/clear_wash_overlay.dart';
part 'zen_types.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const ZenApp());
}

class ZenApp extends StatelessWidget {
  const ZenApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Zen',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: ZenColors.primary),
        useMaterial3: true,
      ),
      home: const ZenScreen(),
    );
  }
}

class ZenScreen extends StatefulWidget {
  const ZenScreen({super.key});

  @override
  State<ZenScreen> createState() => _ZenScreenState();
}

class _ZenScreenState extends State<ZenScreen>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  final GlobalKey _canvasKey = GlobalKey();
  late final ZenSketchModel _sketch;
  late final Ticker _ticker;
  late final AudioPlayer _audioPlayer;
  Timer? _musicStepTimer;
  SharedPreferences? _preferences;
  bool _ready = false;
  bool _menuOpen = false;
  bool _musicEnabled = true;
  bool _painting = false;
  double _musicVolume = SketchMetrics.restingMusicVolume;
  int _clearEffectTick = 0;
  ZenTrack _track = ZenTrack.reachingTheSky;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _sketch = ZenSketchModel();
    _audioPlayer = AudioPlayer();
    _ticker = createTicker((_) => _sketch.tick());
    _bootstrap();
  }

  Future<void> _bootstrap() async {
    final prefs = await SharedPreferences.getInstance();
    final assets = await ZenAssets.load();
    _track = _enumValue(
      ZenTrack.values,
      prefs.getInt(Prefs.track),
      ZenTrack.reachingTheSky,
    );
    await _audioPlayer.setAsset(_track.asset);
    await _audioPlayer.setLoopMode(LoopMode.one);
    await _audioPlayer.setVolume(_musicVolume);
    _preferences = prefs;
    _musicEnabled = prefs.getBool(Prefs.musicEnabled) ?? true;
    _sketch
      ..assets = assets
      ..brushColor = _enumValue(
        BrushColor.values,
        prefs.getInt(Prefs.brushColor),
        BrushColor.dark,
      )
      ..brushSize = _enumValue(
        BrushSize.values,
        prefs.getInt(Prefs.brushSize),
        BrushSize.medium,
      )
      ..flower = _enumValue(
        Flower.values,
        prefs.getInt(Prefs.flower),
        Flower.poppy,
      );
    if (_musicEnabled) {
      unawaited(_audioPlayer.play());
    }
    _ticker.start();
    if (mounted) {
      setState(() => _ready = true);
    }
  }

  T _enumValue<T>(List<T> values, int? index, T fallback) {
    if (index == null || index < 0 || index >= values.length) {
      return fallback;
    }
    return values[index];
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.paused ||
        state == AppLifecycleState.inactive) {
      unawaited(_audioPlayer.pause());
    } else if (state == AppLifecycleState.resumed && _musicEnabled) {
      unawaited(_audioPlayer.play());
    }
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    _musicStepTimer?.cancel();
    _ticker.dispose();
    _audioPlayer.dispose();
    _sketch.dispose();
    super.dispose();
  }

  Future<void> _setTrack(ZenTrack track) async {
    final wasPlaying = _musicEnabled;
    _track = track;
    await _preferences?.setInt(Prefs.track, track.index);
    await _audioPlayer.setAsset(track.asset);
    await _audioPlayer.setLoopMode(LoopMode.one);
    await _audioPlayer.setVolume(_musicVolume);
    if (wasPlaying) {
      await _audioPlayer.play();
    }
    if (mounted) {
      setState(() {});
    }
  }

  Future<void> _setBrushColor(BrushColor color) async {
    setState(() => _sketch.brushColor = color);
    await _preferences?.setInt(Prefs.brushColor, color.index);
  }

  Future<void> _setBrushSize(BrushSize size) async {
    setState(() => _sketch.brushSize = size);
    await _preferences?.setInt(Prefs.brushSize, size.index);
  }

  Future<void> _setFlower(Flower flower) async {
    setState(() => _sketch.flower = flower);
    await _preferences?.setInt(Prefs.flower, flower.index);
  }

  Future<void> _toggleMusic() async {
    _musicEnabled = !_musicEnabled;
    await _preferences?.setBool(Prefs.musicEnabled, _musicEnabled);
    if (_musicEnabled) {
      await _audioPlayer.play();
      _startMusicStepper();
    } else {
      _musicStepTimer?.cancel();
      await _audioPlayer.pause();
    }
    if (mounted) {
      setState(() {});
    }
  }

  void _startMusicStepper() {
    _musicStepTimer?.cancel();
    _musicStepTimer = Timer.periodic(SketchMetrics.musicStepInterval, (_) {
      final target = _painting
          ? SketchMetrics.paintingMusicVolume
          : SketchMetrics.restingMusicVolume;
      if ((_musicVolume - target).abs() < SketchMetrics.musicStep) {
        _musicVolume = target;
      } else if (_musicVolume < target) {
        _musicVolume += SketchMetrics.musicStep;
      } else {
        _musicVolume -= SketchMetrics.musicStep;
      }
      _musicVolume = _musicVolume.clamp(
        SketchMetrics.restingMusicVolume,
        SketchMetrics.paintingMusicVolume,
      );
      unawaited(_audioPlayer.setVolume(_musicVolume));
    });
  }

  void _setPainting(bool painting) {
    if (_painting == painting) {
      return;
    }
    _painting = painting;
    if (_musicEnabled) {
      _startMusicStepper();
    }
  }

  void _pointerDown(PointerDownEvent event) {
    setState(() => _sketch.pointerDown(event));
    _setPainting(true);
  }

  void _pointerMove(PointerMoveEvent event) {
    _sketch.pointerMove(event);
  }

  void _pointerUp(PointerUpEvent event) {
    _sketch.pointerUp(event);
    _setPainting(false);
  }

  void _pointerCancel(PointerCancelEvent event) {
    _sketch.pointerCancel(event);
    _setPainting(false);
  }

  void _undo() {
    setState(_sketch.undo);
  }

  void _clear() {
    setState(() {
      _sketch.clear();
      _clearEffectTick++;
    });
  }

  Future<Uint8List?> _capturePng() async {
    final boundary =
        _canvasKey.currentContext?.findRenderObject() as RenderRepaintBoundary?;
    if (boundary == null) {
      return null;
    }
    final image = await boundary.toImage(pixelRatio: 2);
    final byteData = await image.toByteData(format: ui.ImageByteFormat.png);
    return byteData?.buffer.asUint8List();
  }

  Future<void> _saveImage() async {
    final bytes = await _capturePng();
    if (bytes == null || !mounted) {
      return;
    }
    final directory = await getApplicationDocumentsDirectory();
    final file = File(
      '${directory.path}/zen-${DateTime.now().millisecondsSinceEpoch}.png',
    );
    await file.writeAsBytes(bytes);
    if (mounted) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Saved ${file.path}')));
    }
  }

  Future<void> _shareImage() async {
    final bytes = await _capturePng();
    if (bytes == null) {
      return;
    }
    final directory = await getTemporaryDirectory();
    final file = File('${directory.path}/zen.png');
    await file.writeAsBytes(bytes);
    await SharePlus.instance.share(
      ShareParams(
        files: [XFile(file.path, mimeType: 'image/png')],
        text: 'Zen',
      ),
    );
  }

  void _openBrushOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: Colors.transparent,
      isScrollControlled: true,
      builder: (context) => OptionsSheet(
        title: 'Brush',
        children: [
          OptionsSection(
            label: 'Ink',
            child: HorizontalOptionStrip(
              children: [
                for (final color in BrushColor.values)
                  InkChoice(
                    label: color.label,
                    color: color,
                    selected: _sketch.brushColor == color,
                    onTap: () {
                      Navigator.pop(context);
                      _setBrushColor(color);
                    },
                  ),
              ],
            ),
          ),
          OptionsSection(
            label: 'Weight',
            child: Column(
              children: [
                for (final size in BrushSize.values)
                  WeightChoice(
                    label: size.label,
                    size: size,
                    selected: _sketch.brushSize == size,
                    onTap: () {
                      Navigator.pop(context);
                      _setBrushSize(size);
                    },
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _openFlowerOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: Colors.transparent,
      isScrollControlled: true,
      builder: (context) => OptionsSheet(
        title: 'Flowers',
        children: [
          OptionsSection(
            label: 'Bloom',
            child: Column(
              children: [
                for (final flower in Flower.values)
                  FlowerChoice(
                    flower: flower,
                    selected: _sketch.flower == flower,
                    onTap: () {
                      Navigator.pop(context);
                      _setFlower(flower);
                    },
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _openMusicOptions() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: Colors.transparent,
      isScrollControlled: true,
      builder: (context) => OptionsSheet(
        title: 'Tracks',
        children: [
          OptionsSection(
            label: 'Sound',
            child: Column(
              children: [
                for (final track in ZenTrack.values)
                  TrackChoiceTile(
                    track: track,
                    selected: _track == track,
                    onTap: () {
                      Navigator.pop(context);
                      _setTrack(track);
                    },
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ZenColors.paper,
      body: Stack(
        fit: StackFit.expand,
        children: [
          if (_ready)
            RepaintBoundary(
              key: _canvasKey,
              child: Listener(
                behavior: HitTestBehavior.opaque,
                onPointerDown: _pointerDown,
                onPointerMove: _pointerMove,
                onPointerUp: _pointerUp,
                onPointerCancel: _pointerCancel,
                child: CustomPaint(
                  painter: ZenPainter(_sketch),
                  child: const SizedBox.expand(),
                ),
              ),
            )
          else
            const ColoredBox(color: ZenColors.paper),
          Positioned(
            right: 18,
            bottom: 18,
            child: ZenMenu(
              open: _menuOpen,
              musicEnabled: _musicEnabled,
              canUndo: _sketch.canUndo,
              onToggle: () => setState(() => _menuOpen = !_menuOpen),
              onUndo: _undo,
              onClear: _clear,
              onBrush: _openBrushOptions,
              onFlowers: _openFlowerOptions,
              onMusic: _toggleMusic,
              onTracks: _openMusicOptions,
              onSave: _saveImage,
              onShare: _shareImage,
            ),
          ),
          IgnorePointer(child: ClearWashOverlay(trigger: _clearEffectTick)),
        ],
      ),
    );
  }
}
