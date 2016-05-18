/**
 * 
 */
package com.wellsbi.utils;

import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;


/**
 *
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public class Files {
	private static final Logger log = LoggerFactory.getLogger (Files.class);

    public static Meta meta (String filename, byte[] bytes) {
		try {
			MagicMatch match = Magic.getMagicMatch (bytes); 
			
			// filename may need to be fixed based on the extension
			String extension = FilenameUtils.getExtension(filename);
			String correctExtension = match.getExtension();
			if (StringUtils.isNotEmpty(correctExtension) && 
				! StringUtils.equalsIgnoreCase(extension, correctExtension)) {
		    	log.warn("Uploaded file {} has the wrong extension '{}' - should be '{}'", 
		    			filename, extension, correctExtension);
		    	
		    	// fix the storage location
		    	filename = new StringBuilder (FilenameUtils.removeExtension(filename))
		    			.append(".")
		    			.append(correctExtension)
		    			.toString();
		    	extension = correctExtension;
			} 
			
			return new Meta (match.getMimeType(), extension, filename, Long.valueOf(bytes.length));
		} catch (Throwable t) {
			log.error ("Error parsing mime tyoe for uploaded doc: {}", t.getMessage());
		}
    	
    	return Meta.DEFAULT(filename, Long.valueOf(bytes.length));
    }
    
    public static class Meta {
    	String mimeType;
    	String extension;
    	String filename;
    	String disposition;
    	Long size;
    	String uuid;
    	
    	public static Meta DEFAULT (String filename, Long size) {
    		return new Meta (Defaults.MIME_TYPE, Defaults.EXTENSION, filename, size); 
    	}
    	
    	public Meta(String mimeType, String extension, String filename, Long size) {
    		this.mimeType = mimeType;
    		this.extension = extension;
    		this.filename = filename;
    		this.disposition = new StringBuilder("attachment;filename=")
    			.append(this.filename).toString();
    		this.size = size;
    		this.uuid = UUID.randomUUID().toString();
    	}

    	public String mimeType() { return this.mimeType; }
    	public String extension() { return this.extension; }
    	public String disposition() { return this.disposition; }
    	public String filename() { return this.filename; }
    	public Long size() { return this.size; }
    	public String uuid() { return this.uuid; }

    }


}
