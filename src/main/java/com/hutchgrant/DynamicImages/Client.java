package com.hutchgrant.DynamicImages;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.appengine.api.images.ServingUrlOptions;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Client extends HttpServlet {
	final String bucket = "your-bucket-name";
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		setAccessControlHeaders(resp);
		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		ImageObj img = new ImageObj();
		if (req.getQueryString() != null) {
			img.name = req.getParameter("name");
			img.blobkey = blobstoreService.createGsBlobKey("/gs/" + bucket + "/" + img.name);
			img.size = Integer.parseInt(req.getParameter("size"));
			img.crop = Boolean.parseBoolean(req.getParameter("crop"));
			ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(img.blobkey).imageSize(img.size)
					.crop(img.crop).secureUrl(true);
			img.url = imagesService.getServingUrl(options);

			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String json = gson.toJson(img);

			resp.setContentType("application/json");
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.print(json);
			out.flush();
		} else {
			resp.setContentType("text/plain");
			resp.getWriter().println("App Engine Image API Client");
		}
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		setAccessControlHeaders(resp);

		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();

		if (req.getQueryString() != null) {
			BlobKey key = new BlobKey(req.getParameter("key"));
			imagesService.deleteServingUrl(key);
			out.print("{\"status\": \"success\" }");
		} else {
			out.print("{ \"status\": \"fail\", \"error\": \"invalid parameters, check your query and try again\"}");
		}
		out.flush();
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAccessControlHeaders(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void setAccessControlHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "DELETE, GET");
	}
}
