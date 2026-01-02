# ReVanced HITSTER Database Patch

This repository contains a ReVanced patch for the HITSTER app that allows you to use a custom endpoint for `gameset_database.json` and other configuration files.

## 📋 About

The HITSTER app by default fetches configuration files (including `gameset_database.json`) from `https://hitster.jumboplay.com/hitster-assets/`. This patch allows you to redirect these requests to your own custom server.

## 🎯 Features

- **Custom Endpoint**: Change the base URL for all configuration files
- **Easy Configuration**: Simply modify the `DEFAULT_CUSTOM_URL` constant in the patch
- **Compatible**: Works with HITSTER version 3.1.3

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Gradle
- ReVanced Patcher

### Building

1. Clone this repository:
```bash
git clone https://github.com/Snupai/revanced-hitster-db-patch.git
cd revanced-hitster-db-patch
```

2. **Set up GitHub Packages authentication** (required):
   
   The ReVanced patches plugin is hosted on GitHub Packages and requires authentication.
   
   **Option A: Using environment variables (recommended)**
   ```powershell
   # Set your GitHub username
   $env:GITHUB_ACTOR = "your-github-username"
   
   # Set your GitHub Personal Access Token (with 'read:packages' permission)
   $env:GITHUB_TOKEN = "your-personal-access-token"
   ```
   
   **Option B: Using gradle.properties**
   
   Create or edit `gradle.properties` in the project root:
   ```properties
   gpr.user=your-github-username
   gpr.key=your-personal-access-token
   ```
   
   **To create a GitHub Personal Access Token:**
   1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
   2. Generate new token (classic)
   3. Select scope: `read:packages`
   4. Generate and copy the token
   
   **Note:** You can also copy `gradle.properties.example` to `gradle.properties` and fill in your credentials there.

3. Build the patch:
```bash
./gradlew build
```

### Using the Patch

1. Build the patched APK using ReVanced Manager or CLI
2. Install the patched APK on your device
3. The app will now fetch configuration files from your custom endpoint

## 📁 Server Requirements

Your custom server must serve the following JSON files at the specified paths:

- `gameset_database.json` - Contains deck and card mappings to Spotify track IDs
- `languages.json` - Language configuration
- `countries.json` - Country configuration
- `campaigns.json` - Campaign data
- `translations.json` - Translation strings
- `locales.json` - Locale settings
- `version_info.json` - Version information

### Example Server Structure

```
https://your-custom-server.com/hitster-assets/
├── gameset_database.json
├── languages.json
├── countries.json
├── campaigns.json
├── translations.json
├── locales.json
└── version_info.json
```

## 🔧 How It Works

The patch modifies the Dagger dependency injection component (`DaggerMainApplication_HiltComponents_SingletonC`) to replace the hardcoded base URL string with your custom URL. The app uses Retrofit to make HTTP requests, and the base URL is configured in the Dagger component's `SwitchingProvider.get()` method.

## 📝 Configuration

### Setting the Custom URL

When patching the APK with ReVanced Manager or CLI, you can configure the custom endpoint URL:

**Option Name**: `custom-endpoint-url`  
**Title**: Custom endpoint URL  
**Description**: The base URL for gameset_database.json and other config files. Must end with a forward slash (/).  
**Default**: `https://hitster.jumboplay.com/hitster-assets/`

**Example usage with ReVanced CLI:**
```bash
revanced patch \
  --patch-bundle patches.jar \
  --patch custom-endpoint-url="https://your-server.com/hitster-assets/" \
  --out patched.apk \
  input.apk
```

Make sure the URL:
- Uses HTTPS (or update network security config)
- Ends with a forward slash (`/`)
- Is accessible from the device

## 🛠️ Development

### Project Structure

```
revanced-hitster-db-patch/
├── patches/
│   ├── src/main/kotlin/app/revanced/patches/hitster/
│   │   └── customendpoint/
│   │       ├── CustomEndpointPatch.kt      # Main patch implementation
│   │       └── fingerprints/
│   │           └── BaseUrlFingerprint.kt  # Method fingerprint
│   └── build.gradle.kts
└── README.md
```

### Testing

1. Build the patch
2. Use ReVanced Manager to patch the HITSTER APK
3. Install and test the patched app
4. Monitor network traffic to verify requests go to your custom endpoint

## ⚠️ Important Notes

- **URL Format**: The base URL must end with `/` or the app will throw an `IllegalArgumentException`
- **JSON Structure**: Your `gameset_database.json` must match the expected format from the original server
- **HTTPS**: Use HTTPS for security, or modify the app's network security configuration
- **Compatibility**: This patch is designed for HITSTER version 3.1.3

## 📄 License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

If you encounter any issues or have questions, please open an issue on GitHub.

## 🙏 Acknowledgments

- ReVanced team for the excellent patching framework
- HITSTER app developers
