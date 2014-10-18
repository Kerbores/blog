package com.kerbores.utils.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.http.HttpException;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.sender.PostSender;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;

/**
 * 请勿修改
 * 
 * @author Ixion
 *
 *         create at 2014年8月27日
 */
public class IxionFileUploadPoster extends PostSender {
	public static final String SEPARATOR = "\r\n";

	public IxionFileUploadPoster(Request request) {
		super(request);
	}

	@Override
	public Response send() throws HttpException {
		try {
			String boundary = "---------------------------[Nutz]7d91571440efc";
			openConnection();
			setupRequestHeader();
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			setupDoInputOutputFlag();
			Map<String, Object> params = request.getParams();
			if (null != params && params.size() > 0) {
				DataOutputStream outs = new DataOutputStream(conn.getOutputStream());

				for (Entry<String, ?> entry : params.entrySet()) {
					// outs.writeBytes("--" + boundary + SEPARATOR);
					String key = entry.getKey();
					// 更改以进行文件组上传
					List<File> files = new ArrayList<File>();
					File f = null;
					if (entry.getValue() instanceof File) {
						f = (File) entry.getValue();
						files.add(f);
					} else if (entry.getValue() instanceof File[]) {
						files = Lang.array2list((File[]) entry.getValue());
					} else if (entry.getValue() instanceof Collection<?>) {
						files = Lang.collection2list((Collection<File>) entry.getValue());
					} else {
						outs.writeBytes("--" + boundary + SEPARATOR);
						outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\"\r\n\r\n");
						conn.getOutputStream().write((entry.getValue() + "\r\n").getBytes("UTF-8"));
					}
					if (files.size() > 0) {
						for (File file : files) {
							if (file != null && file.exists()) {
								outs.writeBytes("--" + boundary + SEPARATOR);
								outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\";    filename=\"" + file.getPath()
										+ "\"\r\n");
								outs.writeBytes("Content-Type:   application/octet-stream\r\n\r\n");
								if (file.length() == 0)
									continue;
								InputStream is = Streams.fileIn(file);
								byte[] buffer = new byte[is.available()];
								while (true) {
									int amountRead = is.read(buffer);
									if (amountRead == -1) {
										break;
									}
									outs.write(buffer, 0, amountRead);
									outs.writeBytes("\r\n");
								}
								Streams.safeClose(is);
							}
						}
					}
				}
				outs.writeBytes("--" + boundary + "--" + SEPARATOR);
				Streams.safeFlush(outs);
				Streams.safeClose(outs);
			}

			return createResponse(getResponseHeader());

		} catch (IOException e) {
			throw new HttpException(request.getUrl().toString(), e);
		}
	}
}
