package jp.chikaharu11.instant_drumpad_tr808

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class SaveSlot(
    @PrimaryKey var id: String = "",
    @Required
    var pad: String = "",
    var pad2: String = "",
    var pad3: String = "",
    var pad4: String = "",
    var pad5: String = "",
    var pad6: String = "",
    var pad7: String = "",
    var pad8: String = "",
    var pad9: String = "",
    var pad10: String = "",
    var pad11: String = "",
    var pad12: String = "",
    var pad13: String = "",
    var pad14: String = "",
    var pad15: String = "",
) : RealmObject()