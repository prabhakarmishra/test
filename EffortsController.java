package net.javaguides.springboot.springsecurity.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.demo.vo.Storage;
import com.demo.vo.Vo;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.example.demo.*;

import java.text.DecimalFormat;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class EffortsController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/*
	 * @GetMapping("/") public String index() { return "login"; }
	 */
	/*
	 * @PostMapping("/gcpcal") public String gcpcal() { return "index"; }
	 */
	
	Util util = new Util();
	Utilconfig utilconfig = new Utilconfig();
	DBCalQuery dbquery = new DBCalQuery();
	
	
	
	@PostMapping("/effort")
	public String effortCalculator(Model model, String cname, String dcno, String serverno, String appno, String dbno, String foundation, String foundationType, Double noregion,String security, Double travel, String devops, String backup,  String migration,String mcomplexity, int windowrehost, int windowrebuild, int  windowcon, int linuxrehost, int linuxrebuild, int linuxcon, int sqlhost, int sqlrebuild, int oraclerehost, int oraclerebuild, int postgresrehost, int postgresrebuild, String paas) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {

		  String username = ((UserDetails)principal).getUsername();
		  System.out.println("username==="+username);

		} else {

		  String username = principal.toString();
		  System.out.println("username==="+username);

		}	
		System.out.println("Cusomer name"+cname+"foundation"+foundation+"foundationType"+foundationType+"noregion"+noregion);
		List<effortVO> efforts;
		List<DBFeffort> effort = new ArrayList<>();
		
		
		
		List<DBFeffort> migrationeffort = new ArrayList<>();
		List <String> FEF = new ArrayList<>();
		
		Double onbaseduration = 0.0;
		Double baseduration = 0.0;
		Double offbaseduration = 0.0;
		Double noofhrsweek = 40.0;
		Double noofweek = 0.0;
		Double totalonshorecost = 0.0;
		Double totaloffshorecost = 0.0;
		Double totalcost = 0.0;	
		Double totalonshoreweek = 0.0;
		Double totaloffshoreweek = 0.0;
		Double totalonshorehrs = 0.0;
		Double totaloffshorehrs = 0.0;
		
	
		
		
		Double monbaseduration = 0.0;
		Double mbaseduration = 0.0;
		Double moffbaseduration = 0.0;
		Double mnoofhrsweek = 40.0;
		Double mnoofweek = 0.0;
		Double mtotalonshorecost = 0.0;
		Double mtotaloffshorecost = 0.0;
		Double mtotalcost = 0.0;	
		Double mtotalonshoreweek = 0.0;
		Double mtotaloffshoreweek = 0.0;
		Double mtotalonshorehrs = 0.0;
		Double mtotaloffshorehrs = 0.0;
		
		
		if(foundation.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(foundation))
		{
			
				getFoundationDetails(effort);		
												
				for (int i = 0; i < effort.size(); i++) {
					System.out.println("ResourceName"+effort.get(i).getResourcesname());
					System.out.println("OnSite Rate"+effort.get(i).getOnsite_rate());
					System.out.println("OffShore Rate"+effort.get(i).getOffshore_rate());
					System.out.println("Level"+effort.get(i).getLevel());
					System.out.println(""+effort.get(i).getCategory());
					Double offshorerate = Double.parseDouble(effort.get(i).getOffshore_rate());
					Double onshorerate = Double.parseDouble(effort.get(i).getOnsite_rate());
					
					if(foundationType.equalsIgnoreCase("basic")) {
						
						if(effort.get(i).getBasic_offshore() == null || effort.get(i).getBasic_offshore().trim().isEmpty())
						//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
						{
							offbaseduration = 0.0;
							
						}else {
							offbaseduration=Double.parseDouble(effort.get(i).getBasic_offshore());

						}
						
						if(effort.get(i).getBasic_onshore() == null || effort.get(i).getBasic_onshore().trim().isEmpty())
							//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
							{
								onbaseduration = 0.0;
							}else {
								onbaseduration=Double.parseDouble(effort.get(i).getBasic_onshore());

							}
						
						
					}
					
					if(foundationType.equalsIgnoreCase("basicplus")) {
						if(effort.get(i).getBasicplus_offshore() == null || effort.get(i).getBasicplus_offshore().trim().isEmpty())
							//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
							{
								offbaseduration = 0.0;
								
							}else {
								offbaseduration=Double.parseDouble(effort.get(i).getBasicplus_offshore());

							}
							
							if(effort.get(i).getBasicplus_onshore() == null || effort.get(i).getBasicplus_onshore().trim().isEmpty())
								//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
								{
									onbaseduration = 0.0;
								}else {
									onbaseduration=Double.parseDouble(effort.get(i).getBasicplus_onshore());

								}					
						
					}
					
					if(foundationType.equalsIgnoreCase("premium")) {
						if(effort.get(i).getPremium_offshore() == null || effort.get(i).getPremium_offshore().trim().isEmpty())
							//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
							{
								offbaseduration = 0.0;
								
							}else {
								offbaseduration=Double.parseDouble(effort.get(i).getPremium_offshore());

							}
							
							if(effort.get(i).getPremium_onshore() == null || effort.get(i).getPremium_onshore().trim().isEmpty())
								//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
								{
									onbaseduration = 0.0;
								}else {
									onbaseduration=Double.parseDouble(effort.get(i).getPremium_onshore());

								}
						
						
					}
					
					if(foundationType.equalsIgnoreCase("enterprise")) {
						if(effort.get(i).getEnterprise_offshore() == null || effort.get(i).getEnterprise_offshore().trim().isEmpty())
							//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
							{
								offbaseduration = 0.0;
								
							}else {
								offbaseduration=Double.parseDouble(effort.get(i).getEnterprise_offshore());

							}
							
							if(effort.get(i).getEnterprise_onshore() == null || effort.get(i).getEnterprise_onshore().trim().isEmpty())
								//if(effort.get(i).getBasic_offshore().equalsIgnoreCase("") || "null".equalsIgnoreCase(effort.get(i).getBasic_offshore()))
								{
									onbaseduration = 0.0;
								}else {
									onbaseduration=Double.parseDouble(effort.get(i).getEnterprise_onshore());

								}					
						
					}
						
					if(offbaseduration != 0.0) {					
						
						if(noregion > 2)
						{
							noofweek = offbaseduration + noregion - 2;
						}else {
							noofweek = offbaseduration;					
							
						}						
						effort.get(i).setOffshoreweek(noofweek);
						effort.get(i).setOffshorehrs(noofhrsweek*noofweek);	
						Double offshorecost = noofhrsweek*noofweek*offshorerate;
						effort.get(i).setOffshorecost(offshorecost);
						totaloffshorecost = totaloffshorecost + offshorecost;
						totaloffshoreweek = totaloffshoreweek + noofweek;
						totaloffshorehrs = totaloffshorehrs + (noofhrsweek*noofweek);
						
						
					}
					if(onbaseduration != 0.0) {
					
							onbaseduration=Double.parseDouble(effort.get(i).getBasic_onshore());
							if(noregion > 2)
							{
								noofweek = onbaseduration + noregion - 2;
							}else {
								noofweek = onbaseduration;					
								
							}						
						effort.get(i).setOnshoreweek(noofweek);
						effort.get(i).setOnshorehrs(noofhrsweek*noofweek);	
						Double onshorecost = noofhrsweek*noofweek*onshorerate;
						effort.get(i).setOnshorecost(onshorecost);						
						totalonshorecost = totalonshorecost + onshorecost;
						totalonshoreweek = totalonshoreweek + noofweek;
						totalonshorehrs = totalonshorehrs + (noofhrsweek*noofweek);
						
					}
						
				}

				effort.get(0).setTotaloffshorecost(totaloffshorecost);
				effort.get(0).setTotalonshorecost(totalonshorecost);
				
				effort.get(0).setTotaloffshoreweek(totaloffshoreweek);
				effort.get(0).setTotalonshoreweek(totalonshoreweek);
				
				effort.get(0).setTotaloffshorehrs(totaloffshorehrs);
				effort.get(0).setTotalonshorehrs(totalonshorehrs);
				
				totalcost = totalonshorecost + totaloffshorecost;
				effort.get(0).setTotalcost(totalcost);
				
				effort.get(0).setCname(cname);
				effort.get(0).setDbno(dbno);
				effort.get(0).setDcno(dcno);
				effort.get(0).setAppno(appno);
				effort.get(0).setServerno(serverno);
				
				Double totalweeks = noregion + 5;
				effort.get(0).setTotalweeks(totalweeks);
				
				Double travlecost = 0.0;
				travlecost = (totalcost * travel) /100;
				effort.get(0).setTravelcost(travlecost);
				
				Double finaltotalcost = 0.0;
				finaltotalcost = totalonshorecost + totaloffshorecost + travlecost;
				effort.get(0).setFinalftravelcost(finaltotalcost);	
				
				
				
				model.addAttribute("foundation", "F");
				
				try {
					createDoc(cname);
				}catch(Exception e){
					System.out.println("Exception=="+e);
					
				}
			
		}
		if(migration.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(migration))
		{
			//Double mnoofhrsweek = 40.0;
			
			getMigrationDetails(migrationeffort);
			double complexity = 0;
			
			
			if(mcomplexity.equalsIgnoreCase("simple")) {
				complexity = 1.0;
			}else if(mcomplexity.equalsIgnoreCase("medium")) {
				complexity = 1.1;
			}else if(mcomplexity.equalsIgnoreCase("complex")) {
				complexity = 1.2;
			}else {
				complexity = 1.3;
			}
			
			if(paas.equalsIgnoreCase("Y"))
			{
				
			}
			
			int totalrehost = windowrehost + linuxrehost;
			
			int totalrebuild = windowrebuild + linuxrebuild;
			
			int totalcontainer = windowcon + linuxcon;
			
			
			int basedurationMigration = 1;
			int rehost = 2; // dynamic from db
			int rebuild = 1;
			int totalIaasduration = rehost + rebuild;
			basedurationMigration = basedurationMigration + totalIaasduration;
			basedurationMigration = basedurationMigration * (int)Math.round(complexity);
			System.out.println("basedurationMigration=="+basedurationMigration);
			
			for (int i = 0; i < migrationeffort.size(); i++) {
				System.out.println("migrationeffort ResourceName"+migrationeffort.get(i).getResourcesname());
				System.out.println("OnSite Rate"+migrationeffort.get(i).getOnsite_rate());
				System.out.println("OffShore Rate"+migrationeffort.get(i).getOffshore_rate());
				System.out.println("Level"+migrationeffort.get(i).getLevel());
				System.out.println(""+migrationeffort.get(i).getCategory());
				Double offshorerate = Double.parseDouble(migrationeffort.get(i).getOffshore_rate());
				Double onshorerate = Double.parseDouble(migrationeffort.get(i).getOnsite_rate());
				
				if(migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Project Manager") || migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Cloud Engineer (L3,Mode-2)"))
				{
					
					
					migrationeffort.get(i).setOffshoreweek(Double.valueOf(basedurationMigration));
					migrationeffort.get(i).setOffshorehrs(mnoofhrsweek*basedurationMigration);	
					Double offshorecost = mnoofhrsweek*basedurationMigration*offshorerate;
					migrationeffort.get(i).setOffshorecost(offshorecost);
					mtotaloffshorecost = mtotaloffshorecost + offshorecost;
					mtotaloffshoreweek = mtotaloffshoreweek + basedurationMigration;
					mtotaloffshorehrs = mtotaloffshorehrs + (mnoofhrsweek*basedurationMigration);
					//offbaseduration = basedurationMigration;
					
				} 
				
				if((migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Cloud Infra and DevOps Architect")) && (devops.equalsIgnoreCase("yes")))
				{
					
					
					migrationeffort.get(i).setOffshoreweek(Double.valueOf(basedurationMigration));
					migrationeffort.get(i).setOffshorehrs(mnoofhrsweek*basedurationMigration);	
					Double offshorecost = mnoofhrsweek*basedurationMigration*offshorerate;
					migrationeffort.get(i).setOffshorecost(offshorecost);
					mtotaloffshorecost = mtotaloffshorecost + offshorecost;
					mtotaloffshoreweek = mtotaloffshoreweek + basedurationMigration;
					mtotaloffshorehrs = mtotaloffshorehrs + (mnoofhrsweek*basedurationMigration);
					//offbaseduration = basedurationMigration;
					
				} 
				
				if((migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Security (Subject Matter Expert)")) && (security.equalsIgnoreCase("cloudnative")))
				{
					migrationeffort.get(i).setOffshoreweek(Double.valueOf(basedurationMigration));
					migrationeffort.get(i).setOffshorehrs(mnoofhrsweek*basedurationMigration);	
					Double offshorecost = mnoofhrsweek*basedurationMigration*offshorerate;
					migrationeffort.get(i).setOffshorecost(offshorecost);
					mtotaloffshorecost = mtotaloffshorecost + offshorecost;
					mtotaloffshoreweek = mtotaloffshoreweek + basedurationMigration;
					mtotaloffshorehrs = mtotaloffshorehrs + (mnoofhrsweek*basedurationMigration);
					//offbaseduration = basedurationMigration;
					
				} 
				
				
				if((migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Cloud Architect (PaaS)")) && (paas.equalsIgnoreCase("Y")))
				{				
					
					migrationeffort.get(i).setOffshoreweek(Double.valueOf(basedurationMigration));
					migrationeffort.get(i).setOffshorehrs(mnoofhrsweek*basedurationMigration);	
					Double offshorecost = mnoofhrsweek*basedurationMigration*offshorerate;
					migrationeffort.get(i).setOffshorecost(offshorecost);
					mtotaloffshorecost = mtotaloffshorecost + offshorecost;
					mtotaloffshoreweek = mtotaloffshoreweek + basedurationMigration;
					mtotaloffshorehrs = mtotaloffshorehrs + (mnoofhrsweek*basedurationMigration);
					//offbaseduration = basedurationMigration;
					
				} 
				
				if((migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Backup (Subject Matter Expert)")) && (!backup.equalsIgnoreCase("na")))
				{				
					
					Double basedurationMigrationdev = basedurationMigration/2.0;
					//basedurationMigration = basedurationMigration/2;
					migrationeffort.get(i).setOffshoreweek(Double.valueOf(basedurationMigrationdev));
					migrationeffort.get(i).setOffshorehrs(mnoofhrsweek*basedurationMigrationdev);	
					Double offshorecost = mnoofhrsweek*basedurationMigrationdev*offshorerate;
					migrationeffort.get(i).setOffshorecost(offshorecost);
					mtotaloffshorecost = mtotaloffshorecost + offshorecost;
					mtotaloffshoreweek = mtotaloffshoreweek + basedurationMigrationdev;
					mtotaloffshorehrs = mtotaloffshorehrs + (mnoofhrsweek*basedurationMigrationdev);
					
				} 
				
				
				
				if(migrationeffort.get(i).getResourcesname().equalsIgnoreCase("Cloud Architect (IaaS)"))
				{
					
					
					migrationeffort.get(i).setOnshoreweek(Double.valueOf(basedurationMigration));
					migrationeffort.get(i).setOnshorehrs(mnoofhrsweek*basedurationMigration);	
					Double onshorecost = mnoofhrsweek*basedurationMigration*onshorerate;
					migrationeffort.get(i).setOnshorecost(onshorecost);
					mtotalonshorecost = mtotalonshorecost + onshorecost;
					mtotalonshoreweek = mtotalonshoreweek + basedurationMigration;
					mtotalonshorehrs = mtotalonshorehrs + (mnoofhrsweek*basedurationMigration);	
								
				
					
				}
				
				
				
				
				
			}
						
			migrationeffort.get(0).setTotaloffshorecost(mtotaloffshorecost);
			migrationeffort.get(0).setTotalonshorecost(mtotalonshorecost);
			
			migrationeffort.get(0).setTotaloffshoreweek(mtotaloffshoreweek);
			migrationeffort.get(0).setTotalonshoreweek(mtotalonshoreweek);
			
			migrationeffort.get(0).setTotaloffshorehrs(mtotaloffshorehrs);
			migrationeffort.get(0).setTotalonshorehrs(mtotalonshorehrs);
			
			mtotalcost = mtotalonshorecost + mtotaloffshorecost;
			migrationeffort.get(0).setTotalcost(mtotalcost);
			
			
			
			Double mtravlecost = 0.0;
			mtravlecost = (mtotalcost * travel) /100;
			migrationeffort.get(0).setTravelcost(mtravlecost);
			
			Double mfinaltotalcost = 0.0;
			mfinaltotalcost = mtotalonshorecost + mtotaloffshorecost + mtravlecost;
			migrationeffort.get(0).setFinalftravelcost(mfinaltotalcost);	
			
			
			model.addAttribute("migration", "M");
			
			
			
		}
		
		
		
		
		model.addAttribute("migrationeffort", migrationeffort);
		
		model.addAttribute("effort", effort);
		
		//model.addAttribute("effortMigration", effortMigration);
		model.addAttribute("status", true);
		
		return "demo1";
	}
	
	
	
	
	
	// query methods

	public void getFoundationDetails(List<DBFeffort> effort)
	{
		
		
		     String query = "";
			 query = "select * from foundation_rate where status = '1'";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
					 
							 DBFeffort dbeffort1 = new DBFeffort();
							 dbeffort1.setId(rs.getLong("id"));
							 dbeffort1.setResourcesname(rs.getString("resourcesname"));
							 dbeffort1.setLevel(rs.getString("level"));
							 dbeffort1.setOnsite_rate(rs.getString("onsite_rate"));
							 dbeffort1.setOffshore_rate(rs.getString("offshore_rate"));
							 dbeffort1.setCategory(rs.getString("category"));
							 
							 dbeffort1.setBasic_onshore(rs.getString("basic_onshore"));
							 dbeffort1.setBasicplus_onshore(rs.getString("basicplus_onshore"));
							 dbeffort1.setPremium_onshore(rs.getString("premium_onshore"));
							 dbeffort1.setEnterprise_onshore(rs.getString("enterprise_onshore"));
							 dbeffort1.setBasic_offshore(rs.getString("basic_offshore"));
							 dbeffort1.setBasicplus_offshore(rs.getString("basicplus_offshore"));
							 dbeffort1.setPremium_offshore(rs.getString("premium_offshore"));
							 dbeffort1.setEnterprise_offshore(rs.getString("enterprise_offshore"));	
							 effort.add(dbeffort1);
							
					while(rs.next()){						
						 DBFeffort dbeffort = new DBFeffort();
						 dbeffort.setResourcesname(rs.getString("resourcesname"));
						 dbeffort.setLevel(rs.getString("level"));
						 dbeffort.setOnsite_rate(rs.getString("onsite_rate"));
						 dbeffort.setOffshore_rate(rs.getString("offshore_rate"));
						 dbeffort.setCategory(rs.getString("category"));
						 
						 dbeffort.setBasic_onshore(rs.getString("basic_onshore"));
						 dbeffort.setBasicplus_onshore(rs.getString("basicplus_onshore"));
						 dbeffort.setPremium_onshore(rs.getString("premium_onshore"));
						 dbeffort.setEnterprise_onshore(rs.getString("enterprise_onshore"));
						 dbeffort.setBasic_offshore(rs.getString("basic_offshore"));
						 dbeffort.setBasicplus_offshore(rs.getString("basicplus_offshore"));
						 dbeffort.setPremium_offshore(rs.getString("premium_offshore"));
						 dbeffort.setEnterprise_offshore(rs.getString("enterprise_offshore"));	
						 effort.add(dbeffort);
			        }
					
					 
					return obj; 
			}
			} );}catch (DataAccessException e) 
					{
				//FEF.clear();
				DBFeffort dbeffort = new DBFeffort();
				dbeffort.setResourcesname("No data");
				 
				 effort.add(dbeffort);	
				
			}
	}
			
			
			public void getMigrationDetails(List<DBFeffort> migrationeffort)
			{
				
				
				     String query = "";
					 query = "select * from foundation_rate where status = '1'";
					 System.out.println(query);
				
					try {
					jdbcTemplate.queryForObject(query,new Object[] {},new
							RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
									int rowNum) throws SQLException { Vo obj = new
									Vo();
							 
									 DBFeffort dbeffort1 = new DBFeffort();
									 dbeffort1.setId(rs.getLong("id"));
									 dbeffort1.setResourcesname(rs.getString("resourcesname"));
									 dbeffort1.setLevel(rs.getString("level"));
									 dbeffort1.setOnsite_rate(rs.getString("onsite_rate"));
									 dbeffort1.setOffshore_rate(rs.getString("offshore_rate"));
									 dbeffort1.setCategory(rs.getString("category"));
									 
									 dbeffort1.setBasic_onshore(rs.getString("basic_onshore"));
									 dbeffort1.setBasicplus_onshore(rs.getString("basicplus_onshore"));
									 dbeffort1.setPremium_onshore(rs.getString("premium_onshore"));
									 dbeffort1.setEnterprise_onshore(rs.getString("enterprise_onshore"));
									 dbeffort1.setBasic_offshore(rs.getString("basic_offshore"));
									 dbeffort1.setBasicplus_offshore(rs.getString("basicplus_offshore"));
									 dbeffort1.setPremium_offshore(rs.getString("premium_offshore"));
									 dbeffort1.setEnterprise_offshore(rs.getString("enterprise_offshore"));	
									 migrationeffort.add(dbeffort1);
									
							while(rs.next()){						
								 DBFeffort dbeffort = new DBFeffort();
								 dbeffort.setResourcesname(rs.getString("resourcesname"));
								 dbeffort.setLevel(rs.getString("level"));
								 dbeffort.setOnsite_rate(rs.getString("onsite_rate"));
								 dbeffort.setOffshore_rate(rs.getString("offshore_rate"));
								 dbeffort.setCategory(rs.getString("category"));
								 
								 dbeffort.setBasic_onshore(rs.getString("basic_onshore"));
								 dbeffort.setBasicplus_onshore(rs.getString("basicplus_onshore"));
								 dbeffort.setPremium_onshore(rs.getString("premium_onshore"));
								 dbeffort.setEnterprise_onshore(rs.getString("enterprise_onshore"));
								 dbeffort.setBasic_offshore(rs.getString("basic_offshore"));
								 dbeffort.setBasicplus_offshore(rs.getString("basicplus_offshore"));
								 dbeffort.setPremium_offshore(rs.getString("premium_offshore"));
								 dbeffort.setEnterprise_offshore(rs.getString("enterprise_offshore"));	
								 migrationeffort.add(dbeffort);
					        }
							
							 
							return obj; 
					}
					} );}catch (DataAccessException e) 
							{
						//FEF.clear();
						DBFeffort dbeffort = new DBFeffort();
						dbeffort.setResourcesname("No data");
						 
						migrationeffort.add(dbeffort);	
						
					}
			//return effort;
		
	}
			
			public void createDoc(String customerName) throws Exception
			{

				File file = new File("C:/project/SOW.docx");
				System.out.println(file.getAbsolutePath());
				try {

					/**
					 * if uploaded doc then use HWPF else if uploaded Docx file use XWPFDocument
					 */
					XWPFDocument doc = new XWPFDocument(OPCPackage.open(file.getAbsolutePath()));
					
					
					for (XWPFParagraph p : doc.getParagraphs()) {
						List<XWPFRun> runs = p.getRuns();
						try {
						if (runs != null) {
							for (XWPFRun r : runs) {
								String text = r.getText(0);
								if (text != null && text.contains("<Customer>") || text != null && text.contains("<customer>")) {
									System.out.println("text=="+text);
									text = text.replace("<Customer>", customerName); // your content   
									text = text.replace("<customer>", customerName);
									r.setText(text, 0);
								}
								
								if (text != null && text.contains("<MainCustomer>")) {
									System.out.println("text=="+text);
									text = text.replace("<MainCustomer>", customerName); // your content   <MainCustomer>
									//text = text.replace("<customer>", customerName);
									r.setText(text, 0);
								}
								
								if (text != null && text.contains("<Todays date>")) {
									System.out.println("<Todays date>"+text);
									text = text.replace("<Todays date>", "3-Feb-2021"); // your content
									r.setText(text, 0);
								}
							}
						}
					}
					catch(Exception e) {
						System.out.println("Exception"+e);
						
					}
				}

					doc.write(new FileOutputStream("C:/project/"+customerName+".docx"));
				

				}finally {
					System.out.println("In exception");
					
				}
			}
			
}	
			
	