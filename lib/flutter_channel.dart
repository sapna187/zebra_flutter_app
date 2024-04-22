import 'package:flutter/services.dart';

final MethodChannel _channel = MethodChannel('com.example/zebra_scanner');

Future<String> scanBarcode() async {
  try {
    final String result = await _channel.invokeMethod('scanBarcode');
    return result;
  } catch (e) {
    // Handle any errors
    return 'Error: $e';
  }
  
}
void onScanButtonPressed() async {
  String barcode = await scanBarcode();
  // Handle the scanned barcode as needed
  print('Scanned barcode: $barcode');
}

