package app.revanced.patches.hitster.customendpoint.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.iface.instruction.formats.Instruction31c

object BaseUrlFingerprint : MethodFingerprint(
    "Lnl/q42/hitster/DaggerMainApplication_HiltComponents_SingletonC\$SingletonCImpl\$SwitchingProvider;",
    "get",
    returnType = "Ljava/lang/Object;",
    customFingerprint = { methodDef, _ ->
        methodDef.implementation?.instructions?.any { instruction ->
            instruction is Instruction31c && 
            (instruction.reference as? org.jf.dexlib2.iface.reference.StringReference)?.string == "https://hitster.jumboplay.com/hitster-assets/"
        } == true
    }
)
