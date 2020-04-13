import Foundation

class PreferenceMigration {
    
    func migrate() {
        if UserDefaults.standard.bool(forKey: "flutter_spam.migrate") == false {
            let prefs = UserDefaults.standard.dictionaryRepresentation()
            prefs.forEach { (key, value) in
                edit(key, value: value)
            }
            UserDefaults.standard.set(true, forKey: "flutter_spam.migrate")
            UserDefaults.standard.synchronize()
        }
    }
    
    private func edit(_ key: String, value: Any) {
        let migrateKey = "flutter.\(key)"
        let type = typeOf(value: value)
        
        switch type {
        case .Boolean:
            UserDefaults.standard.set(value as! Bool, forKey: migrateKey)
            break
        case .Int:
            UserDefaults.standard.set(value as! Int, forKey: migrateKey)
            break
        case .Double:
            UserDefaults.standard.set(value as! Double, forKey: migrateKey)
            break
        case .String:
            UserDefaults.standard.set(value as! String, forKey: migrateKey)
            break
        case .StringList:
            UserDefaults.standard.set(value as! [String], forKey: migrateKey)
            break
        default:
            NSLog("This value is no supported : \(value)")
            break
        }
    }
    
    private func typeOf(value: Any) -> ValueType {
        if value is Bool { return ValueType.Boolean }
        if value is Int { return ValueType.Int }
        if value is Double { return ValueType.Double }
        if value is String { return ValueType.String }
        if value is [Any] { return ValueType.StringList }
        // Some of the data will not be migrated
        return ValueType.Unknown
    }

}


