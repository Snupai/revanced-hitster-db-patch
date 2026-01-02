package app.revanced.patches.hitster.customendpoint

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.annotations.Option
import app.revanced.patches.hitster.customendpoint.fingerprints.BaseUrlFingerprint
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.ConstStringInstruction

@Patch(
    name = "Custom gameset database endpoint",
    description = "Allows setting a custom endpoint URL for gameset_database.json and other config files.",
    compatiblePackages = [
        app.revanced.patcher.patch.Patch.CompatiblePackage(
            "nl.jumbo.hitster",
            arrayOf("3.1.3")
        )
    ]
)
object CustomEndpointPatch : BytecodePatch(
    setOf(BaseUrlFingerprint)
) {
    @Option(
        key = "custom-endpoint-url",
        title = "Custom endpoint URL",
        description = "The base URL for gameset_database.json and other config files. Must end with a forward slash (/).",
        default = "https://hitster.jumboplay.com/hitster-assets/"
    )
    var customEndpointUrl: String = "https://hitster.jumboplay.com/hitster-assets/"

    override fun execute(context: BytecodeContext) {
        // Validate URL ends with /
        if (!customEndpointUrl.endsWith("/")) {
            throw PatchException("Custom endpoint URL must end with a forward slash (/)")
        }

        val result = BaseUrlFingerprint.result
            ?: throw PatchException("BaseUrlFingerprint not found")

        val method = result.mutableMethod
        val implementation = method.implementation
            ?: throw PatchException("Method implementation not found")

        // Find the const-string instruction that contains the original URL
        val instructions = implementation.instructions
        var foundIndex = -1
        var targetRegister = -1

        for (i in instructions.indices) {
            val instruction = instructions[i]
            if (instruction is ConstStringInstruction) {
                val stringValue = instruction.string
                if (stringValue == "https://hitster.jumboplay.com/hitster-assets/") {
                    foundIndex = i
                    targetRegister = (instruction as OneRegisterInstruction).registerA
                    break
                }
            }
        }

        if (foundIndex == -1) {
            throw PatchException("Could not find base URL string constant")
        }

        // Replace the string constant with our custom URL
        method.replaceInstruction(
            foundIndex,
            "const-string v$targetRegister, \"$customEndpointUrl\""
        )
    }
}

@Suppress("unused")
val customEndpointPatch = CustomEndpointPatch
