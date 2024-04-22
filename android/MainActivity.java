import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import com.zebra.scannercontrol.*;
import com.zebra.scannercontrol.Scanner;
import com.zebra.scannercontrol.ScannerInfo;
import com.zebra.scannercontrol.ScannerControl;
import com.zebra.scannercontrol.SSApi;
import com.zebra.scannercontrol.BarcodeData;
import com.zebra.scannercontrol.OnEventListener;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.zebra.scannercontrol.app";

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler((call, result) -> {
                if (call.method.equals("scanBarcode")) {
                    handleScanBarcode(result);
                } else {
                    result.notImplemented();
                }
            });
    }

    // private void handleScanBarcode(MethodChannel.Result result) {
    //     // Initialize Zebra SDK and scanner
    //     // Replace 'new Scanner()' with actual scanner initialization from Zebra SDK
    //     Scanner scanner = new Scanner();

    //     // Call SDK method to scan barcode
    //     scanner.scanBarcode(new BarcodeScannerListener() {
    //         @Override
    //         public void onBarcodeScanned(String barcode) {
    //             // Return the scanned barcode back to Flutter
    //             result.success(barcode);
    //         }

    //         @Override
    //         public void onError(Exception e) {
    //             // Return an error back to Flutter
    //             result.error("SCAN_ERROR", e.getMessage(), null);
    //         }
    //     });
    // }
    private void handleScanBarcode(MethodChannel.Result result) {
        // Create a new instance of SSApi (Scanner Software API) to manage the Zebra SDK
        SSApi ssApi = new SSApi(this);
        
        // Open the connection to the SDK
        ssApi.open();
    
        // Retrieve the list of connected scanners
        List<ScannerInfo> scannerList = new ArrayList<>();
        ssApi.getAvailableScannersList(scannerList);
        
        if (scannerList.isEmpty()) {
            // No scanners found
            result.error("NO_SCANNER_FOUND", "No scanners connected", null);
            return;
        }
    
        // Select the first available scanner
        ScannerInfo scannerInfo = scannerList.get(0);
        Scanner scanner = ssApi.getScannerByID(scannerInfo.getScannerID());
    
        if (scanner == null) {
            // Failed to get the scanner
            result.error("SCANNER_ERROR", "Failed to initialize scanner", null);
            return;
        }
    
        // Set up a listener for barcode scans
        scanner.addDataListener((BarcodeData barcodeData) -> {
            // Handle the scanned barcode
            String barcode = barcodeData.getBarcode();
            
            // Return the scanned barcode to Flutter
            result.success(barcode);
            
            // Stop the scanner and clean up
            scanner.stopCapture();
            ssApi.close();
        });
    
        // Start the scanner to capture barcodes
        try {
            scanner.startCapture();
        } catch (Exception e) {
            // Handle any errors during the scanning process
            result.error("SCANNER_ERROR", "Error during scanning: " + e.getMessage(), null);
        }
    }
    
}


