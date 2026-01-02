package app.revanced.patches.hitster.customendpoint.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.iface.instruction.formats.ConstStringInstruction

object BaseUrlFingerprint : MethodFingerprint(
    "Lnl/q42/hitster/DaggerMainApplication_HiltComponents_SingletonC\$SingletonCImpl\$SwitchingProvider;",
    "get",
    returnType = "Ljava/lang/Object;",
    customFingerprint = { methodDef, _ ->
        methodDef.implementation?.instructions?.any { instruction ->
            instruction is ConstStringInstruction &&
            instruction.string == "https://hitster.jumboplay.com/hitster-assets/"
        } == true
    }
)
