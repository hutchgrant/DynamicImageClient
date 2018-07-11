package com.hutchgrant.DynamicImages;

import com.google.appengine.api.blobstore.BlobKey;

public class ImageObj {

	public BlobKey blobkey;
	public String name = "";
	public String url = "";
	public int size = 250;
	public Boolean crop = true;
	public String key = "";

}
