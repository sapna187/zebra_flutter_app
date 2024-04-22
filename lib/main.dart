import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Zebra Scanner App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // final MethodChannel _channel = MethodChannel('com.example/zebra_scanner');
  final MethodChannel _channel = MethodChannel('com.zebra.scannercontrol.app');
  // final MethodChannel _channel = MethodChannel(
  //     'Android_ScannerSDK_v2.6.19.0/barcode_scanner_library_v2.6.19.0-release.aar');
  String _scannedBarcode = '';

  Future<String> scanBarcode() async {
    try {
      final String barcode = await _channel.invokeMethod('scanBarcode');
      return barcode;
    } catch (e) {
      print('Error scanning barcode: $e');
      return '';
    }
  }

  void onScanButtonPressed() async {
    String barcode = await scanBarcode();
    setState(() {
      _scannedBarcode = barcode;
    });
    print('Scanned barcode: $barcode');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Zebra Scanner App'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: onScanButtonPressed,
              child: Text('Open Scanner'),
            ),
            SizedBox(height: 16.0),
            Text('Scanned Barcode: $_scannedBarcode'),
          ],
        ),
      ),
    );
  }
}
