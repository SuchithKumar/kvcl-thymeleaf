package org.vasaviyuvajanasangha.kvcl.utils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import org.imgscalr.Scalr;

public class Helper {

	public static List<String> statesOfIndia(){
		String states = "Andhra Pradesh, Arunachal Pradesh, Assam, Bihar, Chhattisgarh, Goa, Gujarat, Haryana, Himachal Pradesh, Jharkhand, Karnataka, Kerala, Madhya Pradesh, Maharashtra, Manipur, Meghalaya, Mizoram, Nagaland, Odisha, Punjab, Rajasthan, Sikkim, Tamil Nadu, Telangana, Tripura, Uttar Pradesh, Uttarakhand, West Bengal";
		String[] stateArr = states.split(",");
		return Arrays.stream(stateArr).map(a -> a.trim()).toList();
	}
	
	
	BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
	    return Scalr.resize(originalImage, targetWidth);
	}
	
}
