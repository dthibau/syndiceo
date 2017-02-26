package com.syndiceo.util;

import java.awt.Insets;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.Renderer;

import com.syndiceo.Labels;
import com.syndiceo.dto.DemandeDTO;

public class PdfGenerator {

	static public byte[] generateDemandePDF(String serverUrl, DemandeDTO demandeDTO) {

		String  inputFileName = "/workspace/print/demande.xhtml";
//		String  footerTemplate = demandeDTO.getNumero() + " " + demandeDTO.getTitre();

		
		String contentPdf = ""; 
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	
		try {
			if ( Renderer.instance() != null ) {
				Contexts.getEventContext().set("server", serverUrl);
				Contexts.getEventContext().set("demandeDTO", demandeDTO);
				try {
					
					contentPdf = Renderer.instance().render( inputFileName);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				//System.out.print("contentPdf : "+contentPdf);
			}
	
//			PD4ML pd4ml = new PD4ML();
//			pd4ml.setPageInsets(new Insets(15, 15, 20, 15));
//			PD4PageMark footer = new PD4PageMark();
//			footer.setPageNumberAlignment(PD4PageMark.RIGHT_ALIGN);
//			footer.setPageNumberTemplate( Labels.get( "pdf.pageNumber"));
//			footer.setTitleAlignment( PD4PageMark.LEFT_ALIGN);
//			footer.setTitleTemplate( footerTemplate);
//			footer.setAreaHeight(15);
//			footer.setFont(new java.awt.Font("Helvetica",java.awt.Font.ITALIC,10));
//			pd4ml.setPageFooter(footer);
//			pd4ml.render( new StringReader(contentPdf), bos);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Contexts.getEventContext().remove("server");
			Contexts.getEventContext().remove("demande");
		}
		return bos.toByteArray();
	}




}
