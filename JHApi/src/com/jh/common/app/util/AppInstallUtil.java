package com.jh.common.app.util;

import java.io.File;

import com.jh.app.util.BaseToast;
import com.jh.common.app.application.AppSystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppInstallUtil {
	public static void installApp(Context context,String localPath) {
		if (context == null) {
			context = AppSystem.getInstance().getContext();
			if (context == null) {
				return;
			}
		}
		if (localPath != null) {
			try {
				Uri url = Uri.parse(localPath);
				localPath = url.getSchemeSpecificPart();
			} catch (Exception e) {
				e.printStackTrace();
			}

			File file = new File(localPath);
			if (file != null && file.exists()) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				context.startActivity(intent);
			} else {
				BaseToast.getInstance(context, "文件不存在").show();
			}
		}

	}
}
