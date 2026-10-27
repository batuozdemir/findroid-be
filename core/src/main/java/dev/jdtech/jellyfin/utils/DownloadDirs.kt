package dev.jdtech.jellyfin.utils

import android.os.Build
import android.os.Environment
import java.io.File

// Findroid BE: downloads go to the app's private folder on the chosen storage volume (CE's
// behaviour) or, with the "Save to Movies/Findroid" setting and All files access, to
// Movies/Findroid on that volume, where the Files app sees them and uninstalling keeps them.

private const val SHARED_DOWNLOADS_DIR = "Movies/Findroid"

/** The root of the storage volume that holds [storageDir], one of `getExternalFilesDirs`. */
fun storageVolumeRoot(storageDir: File): File =
    File(storageDir.path.substringBefore("/Android/data/"))

/** The downloads folder on the volume of [storageDir], one of `getExternalFilesDirs`. */
fun downloadsFolder(storageDir: File, sharedFolder: Boolean): File =
    if (sharedFolder) File(storageVolumeRoot(storageDir), SHARED_DOWNLOADS_DIR)
    else File(storageDir, "downloads")

/** Whether [dir] is a downloads folder itself, where cleaning up empty folders must stop. */
fun isDownloadsRoot(dir: File): Boolean =
    dir.name == "downloads" || dir.path.endsWith("/$SHARED_DOWNLOADS_DIR")

/** Whether the app holds All files access, which the shared folder needs. */
fun hasAllFilesAccess(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()
