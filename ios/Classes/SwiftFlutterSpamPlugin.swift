import Flutter
import UIKit

public class SwiftFlutterSpamPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let preference = PreferenceMigration()
    preference.migrate()
  }
}
