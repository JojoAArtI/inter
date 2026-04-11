import { PDFDocument, rgb, StandardFonts, PDFFont } from "npm:pdf-lib";

export async function createSimpleTextPdf(resumeJson: Record<string, unknown>): Promise<Uint8Array> {
  const pdfDoc = await PDFDocument.create();
  
  const helveticaFont = await pdfDoc.embedFont(StandardFonts.Helvetica);
  const helveticaBold = await pdfDoc.embedFont(StandardFonts.HelveticaBold);
  
  let page = pdfDoc.addPage([595.28, 841.89]); // A4 Size
  let { width, height } = page.getSize();
  let y = height - 50;
  const margin = 50;
  
  const drawText = (text: string, font: PDFFont, size: number, x: number = margin, isBold: boolean = false) => {
      // Very basic text wrap
      const words = text.split(' ');
      let line = '';
      for(let n = 0; n < words.length; n++) {
          let testLine = line + words[n] + ' ';
          let metrics = font.widthOfTextAtSize(testLine, size);
          if (metrics > width - margin * 2 && n > 0) {
              page.drawText(line, { x, y, size, font: isBold ? helveticaBold : helveticaFont, color: rgb(0, 0, 0) });
              line = words[n] + ' ';
              y -= (size + 4);
          }
          else {
              line = testLine;
          }
      }
      page.drawText(line, { x, y, size, font: isBold ? helveticaBold : helveticaFont, color: rgb(0, 0, 0) });
      y -= (size + 6);
      
      if (y < margin) {
          page = pdfDoc.addPage([595.28, 841.89]);
          y = height - 50;
      }
  };

  const drawHeaderLine = () => {
      y -= 4;
      page.drawLine({
          start: { x: margin, y },
          end: { x: width - margin, y },
          thickness: 1,
          color: rgb(0.7, 0.7, 0.7),
      });
      y -= 10;
  };
  
  const basics = resumeJson.basics as Record<string, any>;
  if (basics) {
      if (basics.name) {
          const nameWidth = helveticaBold.widthOfTextAtSize(basics.name, 24);
          const nameX = (width - nameWidth) / 2;
          page.drawText(basics.name, { x: nameX, y, size: 24, font: helveticaBold, color: rgb(0, 0, 0) });
          y -= 30;
      }
      
      const contacts = [];
      if (basics.email) contacts.push(basics.email);
      if (basics.phone) contacts.push(basics.phone);
      if (basics.location) contacts.push(basics.location);
      if (basics.linkedin) contacts.push(basics.linkedin);
      if (basics.github) contacts.push(basics.github);
      if (basics.portfolio) contacts.push(basics.portfolio);
      
      if(contacts.length > 0) {
          const contactText = contacts.join('  |  ');
          const tw = helveticaFont.widthOfTextAtSize(contactText, 10);
          const tx = (width - tw) / 2;
          page.drawText(contactText, { x: tx, y, size: 10, font: helveticaFont, color: rgb(0.2, 0.2, 0.2) });
          y -= 20;
      }
  }
  
  const addSectionTitle = (title: string) => {
      y -= 10;
      drawText(title.toUpperCase(), helveticaBold, 12, margin, true);
      drawHeaderLine();
  };

  const drawItems = (title: string, items: any[], formatter: (item: any) => void) => {
      if(items && items.length > 0) {
          addSectionTitle(title);
          for(const item of items) {
              formatter(item);
              y -= 4;
          }
      }
  };

  const education = Array.isArray(resumeJson.education) ? resumeJson.education : [];
  drawItems('Education', education, (edu) => {
      drawText(`${edu.school || ''} - ${edu.degree || ''}`, helveticaBold, 11, margin, true);
      drawText(`${edu.start || ''} - ${edu.end || ''} | GPA: ${edu.gpa || 'N/A'}`, helveticaFont, 10, margin);
  });

  const experience = Array.isArray(resumeJson.experience) ? resumeJson.experience : [];
  drawItems('Experience', experience, (exp) => {
      drawText(`${exp.company || ''} - ${exp.role || ''}`, helveticaBold, 11, margin, true);
      drawText(`${exp.start || ''} - ${exp.end || ''}`, helveticaFont, 10, margin);
      if (Array.isArray(exp.bullets)) {
          for(const bullet of exp.bullets) {
              if (typeof bullet === 'string') {
                  drawText(`\u2022 ${bullet}`, helveticaFont, 10, margin + 10);
              }
          }
      }
  });

  const projects = Array.isArray(resumeJson.projects) ? resumeJson.projects : [];
  drawItems('Projects', projects, (proj) => {
      drawText(proj.name || '', helveticaBold, 11, margin, true);
      if (proj.description) {
          drawText(proj.description, helveticaFont, 10, margin);
      }
      if (Array.isArray(proj.highlights)) {
          for(const hl of proj.highlights) {
              if(typeof hl === 'string') {
                  drawText(`\u2022 ${hl}`, helveticaFont, 10, margin + 10);
              }
          }
      }
  });

  const skills = Array.isArray(resumeJson.skills) ? resumeJson.skills : [];
  if (skills.length > 0) {
      addSectionTitle('Skills');
      const skillsText = skills.join(', ');
      drawText(skillsText, helveticaFont, 10, margin);
  }

  const achievements = Array.isArray(resumeJson.achievements) ? resumeJson.achievements : [];
  if (achievements.length > 0) {
      addSectionTitle('Achievements');
      for(const ach of achievements) {
          if (typeof ach === 'string') {
              drawText(`\u2022 ${ach}`, helveticaFont, 10, margin);
          }
      }
  }

  return await pdfDoc.save();
}

export function formatResumeJsonForPdf(resumeJson: Record<string, unknown>): any {
    return resumeJson; // Now passed directly
}
