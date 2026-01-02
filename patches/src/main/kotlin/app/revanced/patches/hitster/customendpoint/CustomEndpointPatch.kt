package app.revanced.patches.hitster.customendpoint

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.PatchException
import app.revanced.patches.hitster.customendpoint.fingerprints.BaseUrlFingerprint
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.ConstStringInstruction

@Suppress("unused")
val customEndpointPatch = bytecodePatch(
    name = "Custom gameset database endpoint",
    description = "Allows setting a custom endpoint URL for gameset_database.json and other config files.",
    packages = listOf("nl.jumbo.hitster"),
    use = true
) {
    execute {
        BaseUrlFingerprint.resolve(
            PatchException("BaseUrlFingerprint not found")
        ).let { result ->
            val method = result.mutableMethod
            val implementation = method.implementation
                ?: throw PatchException("Method implementation not found")

            // Find the const-string instruction that contains the original URL
            val instructions = implementation.instructions
            var foundIndex = -1
            var targetRegister = -1

            for ((index, instruction) in instructions.withIndex()) {
                if (instruction is ConstStringInstruction) {
                    val stringValue = (instruction as ConstStringInstruction).string
                    if (stringValue == "https://hitster.jumboplay.com/hitster-assets/") {
                        foundIndex = index
                        targetRegister = (instruction as OneRegisterInstruction).registerA
                        break
                    }
                }
            }

            if (foundIndex == -1) {
                throw PatchException("Could not find base URL string constant")
            }

            // For now, use default URL - we'll add option support later
            val customUrl = "https://hitster.jumboplay.com/hitster-assets/"

            // Replace the string constant with our custom URL
            method.replaceInstruction(
                foundIndex,
                "const-string v$targetRegister, \"$customUrl\""
            )
        } ?: throw PatchException("BaseUrlFingerprint not found")
    }
}
