package net.javaguides.springboot.springsecurity.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.example.demo.GCPCalVo;
import com.example.demo.*;
import com.example.demo.DBCalQuery;
import java.text.DecimalFormat;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class CalculatorController {

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
	DBCalQuery dbquery = new DBCalQuery();
	
	
	
	@PostMapping("/upload")
	public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model, String prodcommit, String prodstorage,String nonprodcommit, String nonprodstorage,String hrsperday,String daypermonth, String drcommit, String drstorage, String drhrsperday, String drdaypermonth, String cloudstorageregion,Double sstorage,Double nstorage,Double cstorage,Double astorage,String diskregion, Double ssdstorage, Double hssdstorage,String vpnregion, Double vpnvalue,String loadregion, Double frules, Double ingressdata, String egressregion1, String egressregion2, Double egreesst,String dealname,String cname, String vertical, String country, String architect, String email, String dealstage,String dedicatedregion1, Double dedicated1, String dedicatedregion2, Double dedicated2,int partnerdregion, Double partner,String dedicatedregion3,Double dedicated3, String av, Double avquantity,String fw, Double fwquantity,int fwselction, String dr) {
		
		
		/*
		 * App app = new App(); String path = "C:\\work\\GCPInput.xlsx"; InputStream inp
		 * = null; try (Reader reader = new BufferedReader(new
		 * InputStreamReader(file.getInputStream()))) { inp = new FileInputStream(path);
		 * Workbook wb = WorkbookFactory.create(inp);
		 * 
		 * for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		 * System.out.println(wb.getSheetAt(i).getSheetName());
		 * app.convertExcelToCSV(wb.getSheetAt(i), wb.getSheetAt(i).getSheetName()); } }
		 * catch (Exception ex) { System.out.println(ex.getMessage()); }
		 */
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {

		  String username = ((UserDetails)principal).getUsername();
		  System.out.println("username==="+username);

		} else {

		  String username = principal.toString();
		  System.out.println("username==="+username);

		}
		
		// validate file
		if (file.isEmpty()) {
			model.addAttribute("message", "Please select a CSV file to upload.");
			model.addAttribute("status", false);
		} else {

			//model.getAttribute(companyGuid);
			
			// parse CSV file to create a list of `User` objects
			try (
					//file = "C:\\work\\GCPInput.xlsx";
					
					
					//InputStream inputStream       = new FileInputStream("C:\\work\\GCPInput.csv");
				//	Reader      reader = new InputStreamReader(inputStream)
					Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {


				// create csv bean reader
				CsvToBean<GCPCalVo> csvToBean = new CsvToBeanBuilder(reader).withType(GCPCalVo.class)
						.withIgnoreLeadingWhiteSpace(true).build();

				// convert `CsvToBean` object to list of users
				List<GCPCalVo> users = csvToBean.parse();
				//CalVo dCalVo = new CalVo();
				Double finalValue1yrs = 0.0;
				Double finalValue3yrs = 0.0;
				Double finalValueOndemad =0.0;
				Double TotalOndemandyearscost =0.0;
				Double Total1yearscost = 0.0;				
				Double Total3yearscost = 0.0;
				Double TotalLicenseCost = 0.0;
				Double nonProdTotalOndemandyearscost =0.0;
				Double nonProdTotal1yearscost = 0.0;				
				Double nonProdTotal3yearscost = 0.0;
				Double nonProdTotalLicenseCost = 0.0;
				Double licenseValue = 0.0;
				Double clicenseValue = 0.0;
				Double TotalDiskCost = 0.0;
				Double nProdTotalDiskCost = 0.0;
				Double TotalCost = 0.0;
				Double Total1yrsCost = 0.0;
				Double Total3yrsCost = 0.0;
				Double interconnectcost = 0.0;
				Double TotalOndemandinstandardcost = 0.0;
				Double Total1yearsinstandardcost = 0.0;
				Double Total3yearsinstandardcost = 0.0;
				Double TotalLicenseinstandardCost = 0.0;
				Double nonProdTotalOndemandinstandardcost = 0.0;
				Double nonProdTotal1yearsinstandardcost = 0.0;
				Double nonProdTotal3yearsinstandardcost = 0.0;
				Double nonProdTotalLicenseinstandardCost = 0.0;
				
				for (int i = 0; i < users.size(); i++) {
					System.out.println("prodvalue=="+prodcommit+"prodstorage==="+prodstorage+"nonprodvalue=="+nonprodcommit+"nonprodstorage=="+nonprodstorage);
					GCPCalVo gCPCalVo = new GCPCalVo();
					System.out.println("Commitment Type " + users.get(i).getCommitmentType());
					DecimalFormat df = new DecimalFormat("#.##");
					System.out.println("Memory " + users.get(i).getMemory());
					System.out.println("Location " + users.get(i).getLocation());
					System.out.println("CPU " + users.get(i).getvCPU());
					System.out.println("OS " + users.get(i).getOperatingSystem());
					System.out.println("InstanceType " + users.get(i).getInstanceType());
					String instanceType = users.get(i).getInstanceType();
					String instanceTypeActual = users.get(i).getInstanceType();
					String location = users.get(i).getLocation();
					//location = util.checkLocation(location);
					System.out.println("Location"+location);
					if(location.equalsIgnoreCase("Z�rich (europe-west6)"))
					{
						location = "Zurich (europe-west6)";
					}else if(location.equalsIgnoreCase("Montr�al (northamerica-northeast1)")){
						location = "Montreal (northamerica-northeast1)";
					}else if(location.equalsIgnoreCase("S�o Paulo (southamerica-east1)")){
						location = "Sao Paulo (southamerica-east1)";
					}
					System.out.println("Location"+location);
					
					Double totalCPU = users.get(i).getvCPU();
					Double totalmemory = users.get(i).getMemory();
					Double diskspace = users.get(i).getDiskSpace();					
					System.out.println("Environment " + users.get(i).getEnvironment());
					String environment =  users.get(i).getEnvironment();
					String applicationType =  users.get(i).getApplicationType();
					Double hrs = 0.0;
					System.out.println("hrsperday"+hrsperday+"daypermonth"+daypermonth);
					String diskType ="";
					String commitmentType ="";
					String instanceKey = "";
					
					
					System.out.println("environment::"+environment);
					if(environment.equalsIgnoreCase("Prod") || "Prod".equalsIgnoreCase(environment))
					{
						commitmentType =  util.getcommit(prodcommit);
						diskType =  util.getdiskType(prodstorage);	
						hrs = 730.0;
						System.out.println("Prod"+commitmentType+""+diskType+"hrs:"+hrs);
						environment = "Prod";
					}else if(environment.equalsIgnoreCase("DR") || "DR".equalsIgnoreCase(environment)) {
						// DR Logic
						commitmentType =  util.getcommit(drcommit);
						if(drcommit.equalsIgnoreCase("1yrs") || drcommit.equalsIgnoreCase("3yrs"))
						{
							diskType =  util.getdiskType(drstorage);
							hrs = 730.0;
							System.out.println("Non-Prod"+commitmentType+""+diskType+"hrs:"+hrs);
							environment = "DR";
						}else {
							diskType =  util.getdiskType(drstorage);
							if(!"".equalsIgnoreCase(drdaypermonth) || !"".equalsIgnoreCase(drhrsperday)) {								
							Double hrsperdaydouble = Double.parseDouble(drhrsperday);
							Double totalCPUdouble = Double.parseDouble(drdaypermonth);
							//hrs = 176.0;
							hrs = hrsperdaydouble * totalCPUdouble;
												
							} else {
								hrs = 730.0;
							}
							System.out.println("Non-Prod"+commitmentType+""+diskType+"hrs:"+hrs);		
							environment = "DR";
						}
					}
					else //if(environment.equalsIgnoreCase("Dev") || environment.equalsIgnoreCase("Non-Prod") || environment.equalsIgnoreCase("Test") || "QA".equalsIgnoreCase(environment))
					{
						
						commitmentType =  util.getcommit(nonprodcommit);
						diskType =  util.getdiskType(nonprodstorage);
						if(nonprodcommit.equalsIgnoreCase("1yrs") || nonprodcommit.equalsIgnoreCase("3yrs"))
						{
							hrs = 730.0;
							environment = "Non-Prod";
						}else {
							if(!"".equalsIgnoreCase(hrsperday) || !"".equalsIgnoreCase(daypermonth)) {								
							Double hrsperdaydouble = Double.parseDouble(hrsperday);
							Double totalCPUdouble = Double.parseDouble(daypermonth);
							//hrs = 176.0;
							hrs = hrsperdaydouble * totalCPUdouble;
							} else {
								hrs = 730.0;
							}
							System.out.println("Non-Prod"+commitmentType+""+diskType+"hrs:"+hrs);		
							environment = "Non-Prod";
						}
						
					}
					gCPCalVo.setDiskType(diskType);
					users.get(i).setDiskType(diskType);
					gCPCalVo.setCommitmentType(commitmentType);
					users.get(i).setCommitmentType(commitmentType);
					gCPCalVo.setHrs(hrs);
					users.get(i).setHrs(hrs);
					
					/*
					 * String diskType = users.get(i).getDiskType(); String commitmentType =
					 * users.get(i).getCommitmentType(); Double hrs = users.get(i).getHrs();
					 */
					
					// hrs, 
					System.out.println("totalmemory"+totalmemory);
					System.out.println("totalCPU"+totalCPU);
					Double finalAdd = 0.0;	
					List <Double> l1 = new ArrayList<>();
					List <Double> l3 = new ArrayList<>();
					List <Double> lO = new ArrayList<>();
					List <Double> lD = new ArrayList<>();
					List <Double> C1 = new ArrayList<>();
					List <Double> C3 = new ArrayList<>();
					List <Double> CO = new ArrayList<>();
					List <Double> lic = new ArrayList<>();
					List <String> lM = new ArrayList<>();
					List <String> lUD = new ArrayList<>();
					List <String> lUD1 = new ArrayList<>();
					List <Double> ECD = new ArrayList<>();	
					List <Double> Customlist = new ArrayList<>();
					List <Double> M2L = new ArrayList<>();
					List <Double> COM = new ArrayList<>();
					Double discountValue = 0.0;
					String finalValueOn = "Mapping not available";
					String finalValue1 = "Mapping not available";
					String finalValue3 = "Mapping not available";
					String licenseValuefinal ="";
					String licenseValuefinalupanddown = "0";
					String query1 = "";
					String query2 = "";
					String instanceName = "";
					String sinstanceName = "";
					Double showcpu = 0.0;
					Double showmemory = 0.0;
					Double sstandcpu = 0.0;
					Double sstandmemory = 0.0;
					Double cshowcpu = 0.0;
					Double cshowmemory = 0.0;
					Double cshowextmemory = 0.0;
					Double updownshowcpu = 0.0;
					Double updownshowmemory = 0.0;
					String saps = "";
					boolean custom = false;
					boolean instandard15per = false;
					String finalValueOndemadupdown = "Mapping not available";
					String finalValue1upanddown ="Mapping not available";
					String finalValue3upanddown = "Mapping not available";
					String InstanceSummeryupanddown = "Instance Not Found";
					Double sOndemad = 0.0;
					Double s1yrs = 0.0;
					Double s3yrs = 0.0;
					Double sOndemadupdown = 0.0;
					Double s1yrsupdown = 0.0;
					Double s3yrsupdown = 0.0;
					String os = users.get(i).getOperatingSystem();
					if((totalCPU < 0.25) || (totalmemory < 0.5)){
						totalCPU = 99999999.0;
						totalmemory = 99999999.0;
					}if(diskspace <1) {
						diskspace = 0.0;
					}
					
					if(applicationType.equalsIgnoreCase("SAP Apps") && "SAP Apps".equalsIgnoreCase(applicationType)) {
						applicationType = "SAPAPPS";
					}else if(applicationType.equalsIgnoreCase("SAP HANA") && "SAP HANA".equalsIgnoreCase(applicationType)) {
						applicationType = "SAPHANA";
					}else {
						applicationType = "N";
					}
					String instance = "standard";
					String CType = "3year";
					boolean exactstandard = false;
					boolean nextstandard = false;					
					HashMap<String, Double> hm = new HashMap<String, Double>(); 
					HashMap<String, Double> sm = new HashMap<String, Double>();
					HashMap<String, Double> sm2 = new HashMap<String, Double>();
					HashMap<String, Double> ssm = new HashMap<String, Double>();
					HashMap<String, Double> cc = new HashMap<String, Double>(); 
					HashMap<String, Double> costvalue = new HashMap<String, Double>();
					HashMap<String, Double> costssvalue = new HashMap<String, Double>();
					HashMap<String, Double> fixstandardcostvalue = new HashMap<String, Double>();
					HashMap<String, String> instancevalue = new HashMap<String, String>();
					HashMap<String, Double> AllcostCompare = new HashMap<String, Double>();
					HashMap<String, Double> AllcostSCompare = new HashMap<String, Double>();
					HashMap<String, Double> customcostvalue = new HashMap<String, Double>();
					HashMap<String, Double> N2DCustomcostvalue = new HashMap<String, Double>();
					HashMap<String, Double> N2Customcostvalue = new HashMap<String, Double>();
					HashMap<String, Double> N1Customcostvalue = new HashMap<String, Double>();
					HashMap<String, Double> E2Customcostvalue = new HashMap<String, Double>();
					HashMap<String, Double> Svalue = new HashMap<String, Double>();
					HashMap<String, String> SInstancevalue = new HashMap<String, String>();
					HashMap<String, String> Ssapsvalue = new HashMap<String, String>();
					HashMap<String, Double> N1Svalue = new HashMap<String, Double>();
					HashMap<String, Double> E2Svalue = new HashMap<String, Double>();
					HashMap<String, Double> C2Svalue = new HashMap<String, Double>();
					HashMap<String, Double> M1Svalue = new HashMap<String, Double>();
					String queryinstinceType = "'"+instanceType+"'";
					
					if((instanceType.equalsIgnoreCase("Auto Select") && "Auto Select".equalsIgnoreCase(instanceType))) {
						if(environment.equalsIgnoreCase("Non-Prod") || "Non-Prod".equalsIgnoreCase(environment))
						{
							CType = "Ondemand";
							if(applicationType != "N") {
								queryinstinceType = "'E2','N1','N2','N2D','C2','M1','M2'";
							} else {
								queryinstinceType = "'E2','N1','N2','N2D','C2'";									
							}							
						}else {
							CType = "3year";
							if(applicationType != "N") {
								queryinstinceType = "'N1','N2','N2D','C2','M1','M2'";
								} else {
									queryinstinceType = "'N1','N2','N2D','C2'";
								}
						}
						hm =  getMinimumCostSInstance(location, instance, applicationType, CType, totalCPU,totalmemory, queryinstinceType);
					
					}else {
						if(environment.equalsIgnoreCase("Non-Prod") || "Non-Prod".equalsIgnoreCase(environment))
						{
							CType = "Ondemand";
						}
						hm =  getMinimumCostSInstance(location, instance, applicationType, CType, totalCPU,totalmemory, queryinstinceType);
					}
					try {
					Double licenseCost = 0.0;
					String N2DinstanceName = "";
					String N1instanceName = "";
					String scKey = hm.keySet().stream().findFirst().get();
					System.out.println("firstKey=="+scKey);
					if(scKey.equalsIgnoreCase("1") && "1".equalsIgnoreCase(scKey)) {
						sm =  getMinimumCostSNextInstance(location, instance, applicationType, CType, totalCPU,totalmemory, queryinstinceType);
						String nosKey = sm.keySet().stream().findFirst().get();
						if(nosKey.equalsIgnoreCase("1") && "1".equalsIgnoreCase(nosKey)) {
							// check M2 price
							if(applicationType != "N") {								
								getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, "M2", lUD1);
								instanceName  = lUD1.get(0);								
							
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
								/// no standard instance found for M2 also
								}else {
									instanceName  = lUD1.get(0);
									instanceType = lUD1.get(3);
									saps = lUD1.get(4);
									Double scpu = Double.parseDouble(lUD1.get(1));
									Double sramcost = Double.parseDouble(lUD1.get(2));
									sm2 =  getM2Cost(location, instanceName);
									//select * from m2instance where description = 'm2-ultramem-208' and location = 'IOWA (us-central1)'
									sOndemad = sm2.get("Ondemand");
									s1yrs = sm2.get("1year");
									s3yrs = sm2.get("3year");
									finalValueOn = df.format(sOndemad);
									finalValue1 = df.format(s1yrs);
									finalValue3 = df.format(s3yrs);
									instanceName = instanceName + " (vCPUs : " + scpu + ", RAM : "+sramcost+" GB)";
									sinstanceName = instanceName;
									showcpu = scpu;
									showmemory = sramcost;
									saps = saps;
									// OS cost
									licenseValue = getLicenseCostFinalCost(os, scpu, hrs, lic);
									licenseValuefinal = df.format(licenseValue);
									System.out.println("instanceName=="+instanceName);
								}
							}
						}else {
						nextstandard = true;
						String InstanceType = instanceType;
						if(sm.containsKey("E2-Predefined vCPUs")) {
							// E2 cost find and send in map
							InstanceType = "E2";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							instanceName  = lUD1.get(0);
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
							/// no standard instance found
							}else {
								Double E2scpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+instanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,E2scpu, sramcost,applicationType,InstanceType);
								Double N1sOndemad = fixstandardcostvalue.get("sOndemad");
								Double N1s1yrs = fixstandardcostvalue.get("s1yrs");
								Double N1s3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("E2-sOndemad", N1sOndemad);
								Svalue.put("E2-s3yrs", N1s3yrs);
								Svalue.put("E2-s1yrs", N1s1yrs);
								Svalue.put("E2-scpu", E2scpu);
								Svalue.put("E2-memory", sramcost);
								Ssapsvalue.put("E2-saps", saps);
								//N2DSvalue.put("N1instanceName", N1instanceName);
								instanceName = instanceName + " (vCPUs : " + E2scpu + ", RAM : "+sramcost+" GB)";
								AllcostSCompare.put("E2-s3yrs", N1s3yrs);
								SInstancevalue.put("E2instanceName", instanceName);
							}
						} 
						if(sm.containsKey("N2D-Predefined vCPUs")) {
							InstanceType = "N2D";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							N2DinstanceName  = lUD1.get(0);
							if(N2DinstanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(N2DinstanceName)) {
							/// no standard instance found
							}else {
								Double N2Dscpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+N2DinstanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,N2Dscpu, sramcost,applicationType,InstanceType);
								Double N2DsOndemad = fixstandardcostvalue.get("sOndemad");
								Double N2Ds1yrs = fixstandardcostvalue.get("s1yrs");
								Double N2Ds3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("N2D-sOndemad", N2DsOndemad);
								Svalue.put("N2D-s3yrs", N2Ds3yrs);
								Svalue.put("N2D-s1yrs", N2Ds1yrs);
								Svalue.put("N2D-scpu", N2Dscpu);
								N2DinstanceName = N2DinstanceName + " (vCPUs : " + N2Dscpu + ", RAM : "+sramcost+" GB)";
								//N2DSvalue.put("N1instanceName", N1instanceName);
								Svalue.put("N2D-memory", sramcost);
								Ssapsvalue.put("N2D-saps", saps);
								AllcostSCompare.put("N2D-s3yrs", N2Ds3yrs);
								SInstancevalue.put("N2DinstanceName", N2DinstanceName);
						}
						}
						if(sm.containsKey("N1-Predefined vCPUs")) {
							// N1 cost find and send in map
							InstanceType = "N1";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							N1instanceName  = lUD1.get(0);
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
							/// no standard instance found
							}else {
								Double N1scpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+instanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,N1scpu, sramcost,applicationType,InstanceType);
								Double N1sOndemad = fixstandardcostvalue.get("sOndemad");
								Double N1s1yrs = fixstandardcostvalue.get("s1yrs");
								Double N1s3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("N1-sOndemad", N1sOndemad);
								Svalue.put("N1-s3yrs", N1s3yrs);
								Svalue.put("N1-s1yrs", N1s1yrs);
								Svalue.put("N1-scpu", N1scpu);
								Svalue.put("N1-memory", sramcost);
								Ssapsvalue.put("N1-saps", saps);
								//N2DSvalue.put("N1instanceName", N1instanceName);
								N1instanceName = N1instanceName + " (vCPUs : " + N1scpu + ", RAM : "+sramcost+" GB)";
								AllcostSCompare.put("N1-s3yrs", N1s3yrs);
								SInstancevalue.put("N1instanceName", N1instanceName);
						}}
						if(sm.containsKey("N2-Predefined vCPUs")) {
							// N2 cost find and send in map
							InstanceType = "N2";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							String N2instanceName  = lUD1.get(0);
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
							/// no standard instance found
							}else {
								Double N2scpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);	
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+instanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,N2scpu, sramcost,applicationType,InstanceType);
								Double N2sOndemad = fixstandardcostvalue.get("sOndemad");
								Double N2s1yrs = fixstandardcostvalue.get("s1yrs");
								Double N2s3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("N2-sOndemad", N2sOndemad);
								Svalue.put("N2-s3yrs", N2s3yrs);
								Svalue.put("N2-s1yrs", N2s1yrs);
								Svalue.put("N2-scpu", N2scpu);
								Svalue.put("N2-memory", sramcost);
								Ssapsvalue.put("N2-saps", saps);
								//N2DSvalue.put("N1instanceName", N1instanceName);
								N2instanceName = N2instanceName + " (vCPUs : " + N2scpu + ", RAM : "+sramcost+" GB)";
								AllcostSCompare.put("N2-s3yrs", N2s3yrs);
								SInstancevalue.put("N2instanceName", N2instanceName);
							}
						}
						if(sm.containsKey("M1-Predefined vCPUs")) {
							// M1 cost find and send in map
							// N2 cost find and send in map
							InstanceType = "M1";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							String M1instanceName  = lUD1.get(0);
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
							/// no standard instance found
							}else {
								Double m1scpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);	
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+M1instanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,m1scpu, sramcost,applicationType,InstanceType);
								Double N2sOndemad = fixstandardcostvalue.get("sOndemad");
								Double N2s1yrs = fixstandardcostvalue.get("s1yrs");
								Double N2s3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("M1-sOndemad", N2sOndemad);
								Svalue.put("M1-s3yrs", N2s3yrs);
								Svalue.put("M1-s1yrs", N2s1yrs);
								Svalue.put("M1-scpu", m1scpu);
								Svalue.put("M1-memory", sramcost);
								Ssapsvalue.put("M1-saps", saps);
								//N2DSvalue.put("N1instanceName", N1instanceName);
								M1instanceName = M1instanceName + " (vCPUs : " + m1scpu + ", RAM : "+sramcost+" GB)";
								AllcostSCompare.put("M1-s3yrs", N2s3yrs);
								SInstancevalue.put("M1instanceName", M1instanceName);
							}
						}
						if(sm.containsKey("C2-Predefined vCPUs")) {
							// E2 cost find and send in map
							// N2 cost find and send in map
							InstanceType = "C2";
							getInstanceMappingDetailscpu(applicationType, totalCPU, totalmemory, InstanceType, lUD1);
							String C2instanceName  = lUD1.get(0);
							if(instanceName.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instanceName)) {
							/// no standard instance found
							}else {
								Double C2scpu = Double.parseDouble(lUD1.get(1));
								Double sramcost = Double.parseDouble(lUD1.get(2));
								instanceType  = lUD1.get(3);	
								saps  = lUD1.get(4);
								System.out.println("instanceName=="+instanceName);						
								costvalue =  getMinimumCostStdInstance(location, instance, instanceType);
								Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
								// 6 key value pair for get the cost
								String Cost = hmstandardcost.keySet().stream().findFirst().get();					
								fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,C2scpu, sramcost,applicationType,InstanceType);
								Double C2sOndemad = fixstandardcostvalue.get("sOndemad");
								Double C2s1yrs = fixstandardcostvalue.get("s1yrs");
								Double C2s3yrs = fixstandardcostvalue.get("s3yrs");	
								Svalue.put("C2-sOndemad", C2sOndemad);
								Svalue.put("C2-s3yrs", C2s3yrs);
								Svalue.put("C2-s1yrs", C2s1yrs);
								Svalue.put("C2-scpu", C2scpu);
								Svalue.put("C2-memory", sramcost);
								Ssapsvalue.put("C2-saps", saps);
								C2instanceName = C2instanceName + " (vCPUs : " + C2scpu + ", RAM : "+sramcost+" GB)";
								//N2DSvalue.put("N1instanceName", N1instanceName);
								AllcostSCompare.put("C2-s3yrs", C2s3yrs);
								SInstancevalue.put("C2instanceName", C2instanceName);
							}
						}		
							Map<String, Double> hm1 = util.sortByValue(AllcostSCompare); 
							String firstKey = hm1.keySet().stream().findFirst().get();
							System.out.println("firstKey=="+firstKey);
							//String names = "firstKey";
							String[] namesList = firstKey.split("-");
							InstanceType = namesList [0];
							System.out.println("InstanceType=="+InstanceType);
							//namesList [0]+'Svalue'+.get
							s3yrs = Svalue.get(""+InstanceType+"-s3yrs");	
							s1yrs = Svalue.get(""+InstanceType+"-s1yrs");
							sOndemad = Svalue.get(""+InstanceType+"-sOndemad");
							Double cpu = Svalue.get(""+InstanceType+"-scpu");
							showmemory = Svalue.get(""+InstanceType+"-memory");
							saps = Ssapsvalue.get(""+InstanceType+"-saps");
							sinstanceName = SInstancevalue.get(""+InstanceType+"instanceName");
							instanceName = SInstancevalue.get(""+InstanceType+"instanceName");
							finalValueOn = df.format(sOndemad);
							finalValue1 = df.format(s1yrs);
							finalValue3 = df.format(s3yrs);
							showcpu = cpu;
							sstandcpu = showcpu;
							sstandmemory = showmemory;
							// OS cost
							licenseValue = getLicenseCostFinalCost(os, cpu, hrs, lic);
							licenseValuefinal = df.format(licenseValue);
							System.out.println("instanceName=="+instanceName);
						
						}} else {
					exactstandard = true;
					Map<String, Double> hm1 = util.sortByValue(hm); 
					String firstKey = hm1.keySet().stream().findFirst().get();
					System.out.println("firstKey=="+firstKey);
					//String names = "firstKey";
					String[] namesList = firstKey.split("-");
					String InstanceType = namesList [0];
					System.out.println("InstanceType=="+InstanceType);
					getInstanceMapping(applicationType, totalCPU, totalmemory, InstanceType, lM);
					instanceName  = lM.get(0);
					saps  = lM.get(1);
					System.out.println("instanceName=="+instanceName);
					costvalue =  getMinimumCostStdInstance(location, instance, InstanceType);
					Map<String, Double> hmstandardcost = util.sortByValue(costvalue); 
					// 6 key value pair for get the cost
					String Cost = hmstandardcost.keySet().stream().findFirst().get();					
					fixstandardcostvalue = util.calculateAllstandardCost(costvalue,hrs,totalCPU, totalmemory,applicationType,InstanceType);
					instanceName = instanceName + " (vCPUs : " + totalCPU + ", RAM : "+totalmemory+" GB)";
					sinstanceName = instanceName;
					sOndemad = fixstandardcostvalue.get("sOndemad");
					s1yrs = fixstandardcostvalue.get("s1yrs");
					s3yrs = fixstandardcostvalue.get("s3yrs");		
					finalValueOn = df.format(sOndemad);
					finalValue1 = df.format(s1yrs);
					finalValue3 = df.format(s3yrs);
					showcpu = totalCPU;
					showmemory = totalmemory;
					sstandcpu = showcpu;
					sstandmemory = showmemory;
					saps = saps;
					// OS cost
					licenseValue = getLicenseCostFinalCost(os, totalCPU, hrs, lic);
					licenseValuefinal = df.format(licenseValue);
					System.out.println("instanceName=="+instanceName);
					}
					// custom fixed
					System.out.println("custom hello");
					instance = "custom";
					List<Object> instancE2details;
					List<Object> instancdetails;
					List<Object> instancN2Ddetails;
					List<Object> instancN1details;
					List<Object> instancN2details;
					String instanceTypecustom = "";
					Double N2D3yrs = 0.0;
					Double N2DOndemad = 0.0;
					Double N23yrs = 0.0;
					Double N2Ondemad = 0.0;
					Double N13yrs = 0.0;
					Double N1Ondemad = 0.0;
					Double E23yrs = 0.0;
					Double E2Ondemad = 0.0;
					Double cpunew = 0.0;
					String instanceCType = "";
					if((totalCPU <= 96 ) && (totalmemory <=768 && totalmemory >=0.5)) {
					if((instanceTypeActual.equalsIgnoreCase("Auto Select") && "Auto Select".equalsIgnoreCase(instanceTypeActual))) {
						instanceType = "'E2','N1','N2','N2D'";
						cc =  getMinimumCostCInstance(location, instance, applicationType, instanceType);	
						Map<String, Double> cccost = util.sortByValue(cc); 
						// 6 key value pair for get the cost
						String Ccost = cccost.keySet().stream().findFirst().get();
						System.out.println("Ccost=="+Ccost);
						
						if(environment.equalsIgnoreCase("Non-Prod") || "Non-Prod".equalsIgnoreCase(environment))
						{
							CType = "Ondemand";
							instanceType = "E2";
							if(cc.containsKey("E2-Custom vCPUs-3year")) {
							instancE2details = util.getinstacedetails(applicationType, instanceType, instance, totalCPU, totalmemory, Customlist);
							System.out.println("instancdetails.get(0)"+instancE2details.get(0).toString()+"instancdetails.get(1)"+instancE2details.get(1)+"instancdetails.get(2)"+instancE2details.get(2));
							if(instancE2details.get(0).toString().equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instancE2details.get(0).toString()))
							{								
							}else {
								//instancemapping, cpu, finalmemory, extendedmemory
								instanceTypecustom = instancE2details.get(0).toString();
								String cpu = instancE2details.get(1).toString();
								String memory = instancE2details.get(2).toString();
								Double cpue2=Double.parseDouble(cpu);
								Double memorye2=Double.parseDouble(memory);
								String extendedMemoryStr = instancE2details.get(3).toString();		
								Double extendedmemory = Double.parseDouble(extendedMemoryStr);
								E2Customcostvalue = calculateAllCustomCost(cc,hrs,cpue2, memorye2,applicationType,instanceType,extendedmemory,location);
								E23yrs = E2Customcostvalue.get("E2c3yrs");
								E2Ondemad = E2Customcostvalue.get("E2cOndemad");
								AllcostCompare.put("E2-Ondemand", E2Ondemad);
								AllcostCompare.put("E2-3yrs", E23yrs);
							}							
							}							
						}						
						// N2D custom cost					
						
						instanceType = "N2D";
						if(cc.containsKey("N2D-Custom vCPUs-3year")) {
						instancN2Ddetails = util.getinstacedetails(applicationType, instanceType, instance, totalCPU, totalmemory, Customlist);
						System.out.println("instancdetails.get(0)"+instancN2Ddetails.get(0).toString()+"instancdetails.get(1)"+instancN2Ddetails.get(1)+"instancdetails.get(2)"+instancN2Ddetails.get(2));
						if(instancN2Ddetails.get(0).toString().equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instancN2Ddetails.get(0).toString()))
						{								
						}else {
							instanceTypecustom = instancN2Ddetails.get(0).toString();
							String cpu = instancN2Ddetails.get(1).toString();
							String memory = instancN2Ddetails.get(2).toString();
							Double cpun2d=Double.parseDouble(cpu);
							Double memoryn2d=Double.parseDouble(memory);
							String extendedMemoryStr = instancN2Ddetails.get(3).toString();		
							Double extendedmemory = Double.parseDouble(extendedMemoryStr);
							N2DCustomcostvalue = calculateAllCustomCost(cc,hrs,cpun2d, memoryn2d,applicationType,instanceType,extendedmemory,location);
							//if(!N2DCustomcostvalue.isEmpty())
							N2D3yrs = N2DCustomcostvalue.get("N2Dc3yrs");
							N2DOndemad = N2DCustomcostvalue.get("N2DcOndemad");	
							AllcostCompare.put("N2D-Ondemand", N2DOndemad);
							AllcostCompare.put("N2D-3yrs", N2D3yrs);
						}
						}
						// N1 custom cost
						instanceType = "N1";
						if(cc.containsKey("N1-Custom vCPUs-3year")) {
						instancN1details = util.getinstacedetails(applicationType, instanceType, instance, totalCPU, totalmemory, Customlist);
						System.out.println("instancdetails.get(0)"+instancN1details.get(0).toString()+"instancdetails.get(1)"+instancN1details.get(1)+"instancdetails.get(2)"+instancN1details.get(2));
						if(instancN1details.get(0).toString().equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instancN1details.get(0).toString()))
						{								
						}else {
							//instancemapping, cpu, finalmemory, extendedmemory
							instanceTypecustom = instancN1details.get(0).toString();
							String cpu = instancN1details.get(1).toString();
							String memory = instancN1details.get(2).toString();
							Double cpun2d=Double.parseDouble(cpu);
							Double memoryn2d=Double.parseDouble(memory);
							String extendedMemoryStr = instancN1details.get(3).toString();		
							Double extendedmemory = Double.parseDouble(extendedMemoryStr);
							N1Customcostvalue = calculateAllCustomCost(cc,hrs,cpun2d, memoryn2d,applicationType,instanceType,extendedmemory,location);
							N13yrs = N1Customcostvalue.get("N1c3yrs");
							N1Ondemad = N1Customcostvalue.get("N1cOndemad");
							AllcostCompare.put("N1-3yrs", N13yrs);
							AllcostCompare.put("N1-Ondemand", N1Ondemad);
						} }
						// N2 custom cost
						instanceType = "N2";
						if(cc.containsKey("N2-Custom vCPUs-3year")) {
						instancN2details = util.getinstacedetails(applicationType, instanceType, instance, totalCPU, totalmemory, Customlist);
						System.out.println("instancdetails.get(0)"+instancN2details.get(0).toString()+"instancdetails.get(1)"+instancN2details.get(1)+"instancdetails.get(2)"+instancN2details.get(2));
						if(instancN2details.get(0).toString().equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instancN2details.get(0).toString()))
						{								
						}else {
							//instancemapping, cpu, finalmemory, extendedmemory
							instanceTypecustom = instancN2details.get(0).toString();
							String cpu = instancN2details.get(1).toString();
							String memory = instancN2details.get(2).toString();
							Double cpun2d=Double.parseDouble(cpu);
							Double memoryn2d=Double.parseDouble(memory);
							String extendedMemoryStr = instancN2details.get(3).toString();		
							Double extendedmemory = Double.parseDouble(extendedMemoryStr);
							N2Customcostvalue = calculateAllCustomCost(cc,hrs,cpun2d, memoryn2d,applicationType,instanceType,extendedmemory,location);
							N23yrs = N2Customcostvalue.get("N2c3yrs");
							N2Ondemad = N2Customcostvalue.get("N2cOndemad");
							AllcostCompare.put("N2-3yrs", N23yrs);
							AllcostCompare.put("N2-Ondemand", N2Ondemad);
						} }
						if(AllcostCompare.isEmpty()) {
						}else {
						Map<String, Double> customcost = util.sortByValue(AllcostCompare); 
						// 6 key value pair for get the cost
						String Customcost = customcost.keySet().stream().findFirst().get();
						System.out.println("Ccost=="+Customcost);
						String[] customList = Customcost.split("-");
						instanceCType = customList [0];
						System.out.println("InstanceType=="+instanceCType);
						instanceType = instanceCType;
						}
					}
					// get lowest cost instance and get the all values
					else {
						instanceType = instanceTypeActual;
						cc =  getMinimumCostCInstance(location, instance, applicationType, queryinstinceType);	
						
						// 6 key value pair for get the cost
						
					}
					String cKey = cc.keySet().stream().findFirst().get();
					System.out.println("firstKey=="+cKey);
					if(cKey.equalsIgnoreCase("1") && "1".equalsIgnoreCase(cKey)) {
					}else {
					Map<String, Double> cccost = util.sortByValue(cc);
					String Ccost = cccost.keySet().stream().findFirst().get();
					System.out.println("Ccost=="+Ccost);
					instancdetails = util.getinstacedetails(applicationType, instanceType, instance, totalCPU, totalmemory, Customlist);
					System.out.println("instancdetails.get(0)"+instancdetails.get(0).toString()+"instancdetails.get(1)"+instancdetails.get(1)+"instancdetails.get(2)"+instancdetails.get(2));
					if(instancdetails.get(0).toString().equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(instancdetails.get(0).toString()))
					{
						//N23yrs = 0.0;
					}else {
						//instancemapping, cpu, finalmemory, extendedmemory
						instanceTypecustom = instancdetails.get(0).toString();
						String cpu = instancdetails.get(1).toString();
						String memory = instancdetails.get(2).toString();
						cpunew = Double.parseDouble(cpu);
						Double memorynew = Double.parseDouble(memory);
						String extendedMemoryStr = instancdetails.get(3).toString();		
						Double extendedmemory = Double.parseDouble(extendedMemoryStr);
						if(extendedmemory>0) {
							instanceTypecustom = instanceTypecustom + "-extended (vCPUs : " + cpu + ", RAM : "+memory+" GB, extended RAM : "+extendedmemory+" GB)";
							//instanceTypecustom = instanceTypecustom + " (vCPUs : " + cpu + ", RAM : "+memory+" GB)";
						}else {
						instanceTypecustom = instanceTypecustom + " (vCPUs : " + cpu + ", RAM : "+memory+" GB)";
						}
						AllcostCompare = calculateAllCustomCost(cc,hrs,cpunew, memorynew,applicationType,instanceType,extendedmemory,location);
						N2Ondemad = AllcostCompare.get(""+instanceType+"cOndemad");
						N23yrs = AllcostCompare.get(""+instanceType+"c3yrs");
						Double yr3cpu = costvalue.get(""+instanceType+"-Custom vCPUs-3year");
						cshowcpu = cpunew;
						cshowmemory = memorynew;
						cshowextmemory = extendedmemory;
						System.out.println("New custom value"+N23yrs);						
					}				
					}						
				}
					
				if(environment.equalsIgnoreCase("Non-Prod") || "Non-Prod".equalsIgnoreCase(environment))
				{
					if((N2Ondemad > 0) && (N2Ondemad < sOndemad))
					{
						System.out.println("Lowest custom cost ondemad== "+N23yrs + "standard ondemad cost=="+ sOndemad);
						Double cOndemad = AllcostCompare.get(""+instanceType+"cOndemad");
						Double c1yrs = AllcostCompare.get(""+instanceType+"c1yrs");
						Double c3yrs = AllcostCompare.get(""+instanceType+"c3yrs");
						instanceName = instanceTypecustom;
						finalValueOn = df.format(cOndemad);
						finalValue1 = df.format(c1yrs);
						finalValue3 = df.format(c3yrs);
						showcpu = cshowcpu;
						showmemory = cshowmemory + cshowextmemory;
						// license cost
						clicenseValue = getLicenseCostFinalCost(os, cpunew, hrs, lic);
						licenseValuefinal = df.format(clicenseValue);					
					} 
					
				} else {
				if((N23yrs > 0) && (N23yrs < s3yrs))
				{
					System.out.println("Lowest custom cost 3 yrs== "+N23yrs + "standard 3 years cost=="+ s3yrs);
					Double cOndemad = AllcostCompare.get(""+instanceType+"cOndemad");
					Double c1yrs = AllcostCompare.get(""+instanceType+"c1yrs");
					Double c3yrs = AllcostCompare.get(""+instanceType+"c3yrs");
					instanceName = instanceTypecustom;
					finalValueOn = df.format(cOndemad);
					finalValue1 = df.format(c1yrs);
					finalValue3 = df.format(c3yrs);
					showcpu = cshowcpu;
					showmemory = cshowmemory + cshowextmemory;
					// license cost
					clicenseValue = getLicenseCostFinalCost(os, cpunew, hrs, lic);
					licenseValuefinal = df.format(clicenseValue);					
				} 
				}
				
					// up and down standard only
				instance = "standard";
				if((exactstandard) && (s3yrs > 0)) {
					InstanceSummeryupanddown = sinstanceName;			
					finalValueOndemadupdown = df.format(sOndemad);;
					finalValue1upanddown = df.format(s1yrs);
					finalValue3upanddown = df.format(s3yrs);
					updownshowcpu = sstandcpu;
					updownshowmemory = sstandmemory;
					
					licenseValuefinalupanddown = licenseValuefinal;
				} else if(instanceType.equalsIgnoreCase("M2")) {
					InstanceSummeryupanddown = sinstanceName;			
					finalValueOndemadupdown = df.format(sOndemad);;
					finalValue1upanddown = df.format(s1yrs);
					finalValue3upanddown = df.format(s3yrs);
					updownshowcpu = sstandcpu;
					updownshowmemory = sstandmemory;
					licenseValuefinalupanddown = licenseValuefinal;				
				}else {
					// + / - 15 % 
					Double minvCPU = totalCPU - totalCPU*15/100;
				    Double maxvCPU = totalCPU + totalCPU*50/100;
				    Double minmemory = totalmemory - totalmemory*20/100;
				    Double maxmemory = totalmemory + totalmemory*70/100;
					ssm =  getMinimumCostIncreasePerInstance(location, instance, applicationType, CType, minvCPU, maxvCPU, minmemory, maxmemory, queryinstinceType);
					String nossKey = ssm.keySet().stream().findFirst().get();
					System.out.println("firstKey=="+nossKey);
					if((nossKey.equalsIgnoreCase("1")) || ("1".equalsIgnoreCase(nossKey))) {
							InstanceSummeryupanddown = sinstanceName;			
							finalValueOndemadupdown = df.format(sOndemad);;
							finalValue1upanddown = df.format(s1yrs);
							finalValue3upanddown = df.format(s3yrs);
							updownshowcpu = sstandcpu;
							updownshowmemory = sstandmemory;
							licenseValuefinalupanddown = licenseValuefinal;
					
					}else {
					Map<String, Double> ssm1 = util.sortByValue(ssm); 						
					String ssKey = ssm1.keySet().stream().findFirst().get();
					System.out.println("firstKey=="+ssKey);
					//String names = "firstKey";
					String[] namesList = ssKey.split("-");
					String InstanceType = namesList [0];
					System.out.println("InstanceType=="+InstanceType);
					getInstanceMappingDetailsStandardcpu(applicationType, minvCPU, maxvCPU, minmemory, maxmemory, InstanceType, lUD1);
					InstanceSummeryupanddown  = lUD1.get(0);
					if(InstanceSummeryupanddown.equalsIgnoreCase("No Instance Found") || "No Instance Found".equalsIgnoreCase(InstanceSummeryupanddown)) {
					/// no standard instance found
						InstanceSummeryupanddown = sinstanceName;			
						finalValueOndemadupdown = df.format(sOndemad);;
						finalValue1upanddown = df.format(s1yrs);
						finalValue3upanddown = df.format(s3yrs);
						updownshowcpu = sstandcpu;
						updownshowmemory = sstandmemory;
						licenseValuefinalupanddown = licenseValuefinal;
					}else {
					Double sscpu = Double.parseDouble(lUD1.get(1));
					Double ssramcost = Double.parseDouble(lUD1.get(2));
					instanceType  = lUD1.get(3);	
					InstanceSummeryupanddown = InstanceSummeryupanddown + " (vCPUs : " + sscpu + ", RAM : "+ssramcost+" GB)";
					System.out.println("instanceName=="+InstanceSummeryupanddown);						
					costssvalue =  getMinimumCostStdInstance(location, instance, InstanceType);
					Map<String, Double> hmstandardcost = util.sortByValue(costssvalue); 
					// 6 key value pair for get the cost
					String Cost = hmstandardcost.keySet().stream().findFirst().get();					
					fixstandardcostvalue = util.calculateAllstandardCost(costssvalue,hrs,sscpu, ssramcost,applicationType,InstanceType);
					sOndemadupdown = fixstandardcostvalue.get("sOndemad");
					s1yrsupdown = fixstandardcostvalue.get("s1yrs");
					s3yrsupdown = fixstandardcostvalue.get("s3yrs");
					if(((s3yrsupdown >0) && (s3yrsupdown < s3yrs))) {
					//instanceName = InstanceSummeryupanddown;
					finalValueOndemadupdown = df.format(sOndemadupdown);
					finalValue1upanddown = df.format(s1yrsupdown);
					finalValue3upanddown = df.format(s3yrsupdown);
					updownshowcpu = sscpu;
					updownshowmemory = ssramcost;
					// OS cost
					licenseValue = getLicenseCostFinalCost(os, sscpu, hrs, lic);
					licenseValuefinalupanddown = df.format(licenseValue);
					System.out.println("instanceName=="+InstanceSummeryupanddown);
					} else if(((s3yrsupdown >0) && (s3yrs >= 0))) {
						//instanceName = InstanceSummeryupanddown;
						finalValueOndemadupdown = df.format(sOndemadupdown);
						finalValue1upanddown = df.format(s1yrsupdown);
						finalValue3upanddown = df.format(s3yrsupdown);
						updownshowcpu = sscpu;
						updownshowmemory = ssramcost;
						// OS cost
						licenseValue = getLicenseCostFinalCost(os, sscpu, hrs, lic);
						licenseValuefinalupanddown = df.format(licenseValue);
						System.out.println("instanceName=="+InstanceSummeryupanddown);
						}else {
						InstanceSummeryupanddown = sinstanceName;			
						finalValueOndemadupdown = df.format(sOndemad);;
						finalValue1upanddown = df.format(s1yrs);
						finalValue3upanddown = df.format(s3yrs);
						updownshowcpu = showcpu;
						updownshowmemory = showmemory;
						licenseValuefinalupanddown = licenseValuefinal;
						
						///           Azure compare data
						
						/*Double azmemory = totalmemory * 1024;
						String alocation = "US East 2";
						HashMap<String, Double> hma = new HashMap<String, Double>();
						hma =  getAzureInstanceMapping(alocation, instance, applicationType, CType, totalCPU,azmemory, queryinstinceType);
						*/
						
					}
				}
				}
				}
				}catch(Exception e)
					{
						System.out.println("Exception=="+e);
					}
					System.out.println("skuName=="+instanceName);
					gCPCalVo.setInstanceMapping(instanceName);					
					users.get(i).setInstanceMapping(instanceName);
					
					
					// total cost = ram +cpu Double finalValueOndemad = 0.0;
					// ondemand cost
					//System.out.println("query1"+query2);
				    
				    gCPCalVo.setShowcpu(showcpu);
				    users.get(i).setShowcpu(showcpu);
				    
				    gCPCalVo.setShowmemory(showmemory);
				    users.get(i).setShowmemory(showmemory);
				    
				    gCPCalVo.setUpdownshowcpu(updownshowcpu);
				    users.get(i).setUpdownshowcpu(updownshowcpu);
				    
				    gCPCalVo.setUpdownshowmemory(updownshowmemory);
				    users.get(i).setUpdownshowmemory(updownshowmemory);
				    
				    gCPCalVo.setSaps(saps);
				    users.get(i).setSaps(saps);
				    
				    gCPCalVo.setOnDemandCost(finalValueOn);
				    users.get(i).setOnDemandCost(finalValueOn);
				
				 // 1 year cost
				    
					gCPCalVo.setOneyrsCost(finalValue1);						
					users.get(i).setOneyrsCost(finalValue1);
					// total cost = ram +cpu
					// 3 year cost					
					
					gCPCalVo.setThreeyrsCost(finalValue3);						
					users.get(i).setThreeyrsCost(finalValue3);
				
					// disk details
					String diskcommitmentType = "";
					getdiskCost(location, diskType, diskspace, lD);
					
					//disk cost
					Double diskValue = lD.get(0);	
					System.out.println("Final disk cost cost "+diskValue);
					String diskValuefinal = df.format(diskValue);
					gCPCalVo.setDiskCost(diskValuefinal);
					users.get(i).setDiskCost(diskValuefinal);
					
					
					gCPCalVo.setLicenseCost(licenseValuefinal);
					users.get(i).setLicenseCost(licenseValuefinal);
					
					// ondemand up and down
					 gCPCalVo.setInstanceSummeryupanddown(InstanceSummeryupanddown);
					 users.get(i).setInstanceSummeryupanddown(InstanceSummeryupanddown);	
					
					// ondemand up and down
					 gCPCalVo.setOnDemandCostupanddown(finalValueOndemadupdown);
					 users.get(i).setOnDemandCostupanddown(finalValueOndemadupdown);	
					 // 1 year cost
					 
					gCPCalVo.setOneyrsCostUpandDown(finalValue1upanddown);						
					users.get(i).setOneyrsCostUpandDown(finalValue1upanddown);
					// 3 year cost					
						
					gCPCalVo.setThreeyrsCostUpandDown(finalValue3upanddown);						
					users.get(i).setThreeyrsCostUpandDown(finalValue3upanddown);
					
					// license cost
					gCPCalVo.setLicenseCostupanddown(licenseValuefinalupanddown);
					users.get(i).setLicenseCostupanddown(licenseValuefinalupanddown);
					
					if(environment.equalsIgnoreCase("Prod") || "Prod".equalsIgnoreCase(environment))
					{
						System.out.println("finalValueOn::"+finalValueOn+"finalValue1::"+finalValue1+"finalValue3::"+finalValueOn);
						if("Mapping not available".equalsIgnoreCase(finalValueOn) || "".equalsIgnoreCase(finalValueOn)) {						
						}else {
							TotalOndemandyearscost = Double.parseDouble(finalValueOn) + TotalOndemandyearscost;
							System.out.println("finalValueOndemad"+finalValueOn+"Total1yearscost"+TotalOndemandyearscost);
						}
						if("Mapping not available".equalsIgnoreCase(finalValue1) || "".equalsIgnoreCase(finalValue1)) {						
						}else {
							Total1yearscost = Double.parseDouble(finalValue1) + Total1yearscost;
							System.out.println("finalValue1yrs"+finalValue1+"Total1yearscost"+Total1yearscost);
						}
						if("Mapping not available".equalsIgnoreCase(finalValue3) || "".equalsIgnoreCase(finalValue3)) {						
						}else {
							Total3yearscost = Double.parseDouble(finalValue3) + Total3yearscost;
							System.out.println("finalValue1yrs"+finalValue3+"Total1yearscost"+Total3yearscost);
						}
						
						System.out.println("licenseValuefinal=="+licenseValuefinal);
						if("".equalsIgnoreCase(licenseValuefinal)) {						
						}else {
						TotalLicenseCost =	Double.parseDouble(licenseValuefinal) + TotalLicenseCost;
						}
					}else {
							System.out.println("finalValueOn::"+finalValueOn+"finalValue1::"+finalValue1+"finalValue3::"+finalValueOn);
							if("Mapping not available".equalsIgnoreCase(finalValueOn) || "".equalsIgnoreCase(finalValueOn)) {						
							}else {
								nonProdTotalOndemandyearscost = Double.parseDouble(finalValueOn) + nonProdTotalOndemandyearscost;
								System.out.println("finalValueOndemad"+finalValueOn+"nonProdTotalOndemandyearscost"+nonProdTotalOndemandyearscost);
							}
							if("Mapping not available".equalsIgnoreCase(finalValue1) || "".equalsIgnoreCase(finalValue1)) {						
							}else {
								nonProdTotal1yearscost = Double.parseDouble(finalValue1) + nonProdTotal1yearscost;
								System.out.println("finalValue1yrs"+finalValue1+"nonProdTotal1yearscost"+nonProdTotal1yearscost);
							}
							if("Mapping not available".equalsIgnoreCase(finalValue3) || "".equalsIgnoreCase(finalValue3)) {						
							}else {
								nonProdTotal3yearscost = Double.parseDouble(finalValue3) + nonProdTotal3yearscost;
								System.out.println("finalValue1yrs"+finalValue3+"nonProdTotal3yearscost"+nonProdTotal3yearscost);
							}
							
							System.out.println("licenseValuefinal=="+licenseValuefinal);
							if("".equalsIgnoreCase(licenseValuefinal)) {						
							}else {
							nonProdTotalLicenseCost =	Double.parseDouble(licenseValuefinal) + nonProdTotalLicenseCost;
							}
						
					}
					/*
					 * if(licenseValuefinal.equalsIgnoreCase("")) { TotalLicenseCost =
					 * Double.parseDouble(licenseValuefinal) + TotalLicenseCost; }
					 * System.out.println("licenseValue"+licenseValuefinal+"TotalLicenseCost"+
					 * TotalLicenseCost);
					 */
					if(environment.equalsIgnoreCase("Prod") || "Prod".equalsIgnoreCase(environment))
					{
						TotalDiskCost = diskValue + TotalDiskCost;
						System.out.println("diskValue"+diskValue+"TotalDiskCost"+TotalDiskCost);					
					
					if("Mapping not available".equalsIgnoreCase(finalValue1upanddown) || "".equalsIgnoreCase(finalValue1upanddown)) {						
					}else {
						TotalOndemandinstandardcost = Double.parseDouble(finalValueOndemadupdown) + TotalOndemandinstandardcost;
						System.out.println("standard cost finalValueOndemad"+finalValueOndemadupdown+"Total1yearscost"+TotalOndemandinstandardcost);
					}
					if("Mapping not available".equalsIgnoreCase(finalValue1upanddown) || "".equalsIgnoreCase(finalValue1upanddown)) {						
					}else {
						Total1yearsinstandardcost = Double.parseDouble(finalValue1upanddown) + Total1yearsinstandardcost;
						System.out.println("standard cost  finalValue1yrs"+finalValue1upanddown+"Total1yearscost"+Total1yearsinstandardcost);
					}
					if("Mapping not available".equalsIgnoreCase(finalValue3upanddown) || "".equalsIgnoreCase(finalValue3upanddown)) {						
					}else {
						Total3yearsinstandardcost = Double.parseDouble(finalValue3upanddown) + Total3yearsinstandardcost;
						System.out.println("standard cost  finalValue1yrs"+finalValue3upanddown+"Total1yearscost"+Total3yearsinstandardcost);
					}
					
					if("".equalsIgnoreCase(licenseValuefinalupanddown)) {						
					}else {
						TotalLicenseinstandardCost = Double.parseDouble(licenseValuefinalupanddown) + TotalLicenseinstandardCost;
						System.out.println("standard cost licenseValue"+licenseValuefinalupanddown+"TotalLicenseCost"+TotalLicenseinstandardCost);
					}
					}else {
						
						if("Mapping not available".equalsIgnoreCase(finalValue1upanddown) || "".equalsIgnoreCase(finalValue1upanddown)) {						
						}else {
							nonProdTotalOndemandinstandardcost = Double.parseDouble(finalValueOndemadupdown) + nonProdTotalOndemandinstandardcost;
							System.out.println("standard cost finalValueOndemad"+finalValueOndemadupdown+"Total1yearscost"+nonProdTotalOndemandinstandardcost);
						}
						if("Mapping not available".equalsIgnoreCase(finalValue1upanddown) || "".equalsIgnoreCase(finalValue1upanddown)) {						
						}else {
							nonProdTotal1yearsinstandardcost = Double.parseDouble(finalValue1upanddown) + nonProdTotal1yearsinstandardcost;
							System.out.println("standard cost  finalValue1yrs"+finalValue1upanddown+"Total1yearscost non prod"+nonProdTotal1yearsinstandardcost);
						}
						if("Mapping not available".equalsIgnoreCase(finalValue3upanddown) || "".equalsIgnoreCase(finalValue3upanddown)) {						
						}else {
							nonProdTotal3yearsinstandardcost = Double.parseDouble(finalValue3upanddown) + nonProdTotal3yearsinstandardcost;
							System.out.println("standard cost  finalValue1yrs"+finalValue3upanddown+"Total1yearscost"+nonProdTotal3yearsinstandardcost);
						}
						
						if("".equalsIgnoreCase(licenseValuefinalupanddown)) {						
						}else {
							nonProdTotalLicenseinstandardCost = Double.parseDouble(licenseValuefinalupanddown) + nonProdTotalLicenseinstandardCost;
							System.out.println("standard cost licenseValue"+licenseValuefinalupanddown+"TotalLicenseCost"+nonProdTotalLicenseinstandardCost);
						}						
						nProdTotalDiskCost = diskValue + nProdTotalDiskCost;
						System.out.println("diskValue"+diskValue+"nProdTotalDiskCost"+nProdTotalDiskCost);
					}
				}
				System.out.println("TotalOndemandyearscost"+TotalOndemandyearscost);
			    users.get(0).setTotalOndemandyearscost(TotalOndemandyearscost);
				System.out.println("finalValue1yrs"+Total1yearscost);			    
			    users.get(0).setTotal1yrscost(Total1yearscost);
			    users.get(0).setTotal3yearscost(Total3yearscost);
			    System.out.println("finalValue3yrs"+Total3yearscost);
			    users.get(0).setTotallicenseCost(TotalLicenseCost);
			    System.out.println("TotalLicenseCost"+Total3yearscost);
			    users.get(0).setTotallicenseCost(TotalLicenseCost);
			    System.out.println("TotalLicenseCost"+TotalLicenseCost);
			    
			    users.get(0).setNonProdTotalOndemandyearscost(nonProdTotalOndemandyearscost);
			    users.get(0).setNonProdtotal1yrscost(nonProdTotal1yearscost);
			    users.get(0).setNonProdTotal3yearscost(nonProdTotal3yearscost);
			    users.get(0).setNonProdTotallicenseCost(nonProdTotalLicenseCost);
			    
			    System.out.println("TotalDiskCost"+TotalDiskCost);
			    users.get(0).setTotalDiskCost(TotalDiskCost);
			    System.out.println("Totalnonprod disk cost"+nProdTotalDiskCost);
			    users.get(0).setNonProdTotalDiskCost(nProdTotalDiskCost);
			    
			    users.get(0).setTotalOndemandinstandardcost(TotalOndemandinstandardcost);
			    users.get(0).setTotal1yearsinstandardcost(Total1yearsinstandardcost);
			    users.get(0).setTotal3yearsinstandardcost(Total3yearsinstandardcost);
			    users.get(0).setTotalLicenseinstandardCost(TotalLicenseinstandardCost);
			    
			    users.get(0).setNonProdTotalOndemandinstandardcost(nonProdTotalOndemandinstandardcost);
			    users.get(0).setNonProdTotal1yearsinstandardcost(nonProdTotal1yearsinstandardcost);
			    users.get(0).setNonProdTotal3yearsinstandardcost(nonProdTotal3yearsinstandardcost);
			    users.get(0).setNonProdTotalLicenseinstandardCost(nonProdTotalLicenseinstandardCost);
			    
				List<CalVo> storage = new ArrayList<>();
				List <Double> cloudl = new ArrayList<>();
				List <Double> cloud2 = new ArrayList<>();
				List <Double> cloud3 = new ArrayList<>();
				List <Double> cloud4 = new ArrayList<>();
				List <Double> cloud5 = new ArrayList<>();
				List <Double> cloud6 = new ArrayList<>();
				List <Double> vpnlist = new ArrayList<>();
				List <Double> cloudload = new ArrayList<>();
				List <Double> cloudingress = new ArrayList<>();
				CalVo gCalVo = new CalVo();
				Double finalSstorage = 0.0;
				Double finalAstorage =0.0;
				Double finalCstorage = 0.0;
				Double finalSstorage2 = 0.0;
				Double fingresscost = 0.0;
				Double totalcostegress = 0.0;
				System.out.println("cloudstorageregion"+cloudstorageregion+"sstorage"+sstorage+"nstorage"+nstorage+"cstorage"+cstorage+"astorage"+astorage);
				if(!sstorage.isNaN()) {
				System.out.println("sstorage is not null");
				getCloudStorageValue(cloudstorageregion , sstorage, cloudl);
				System.out.println("SStoarage Value"+cloudl.get(0));
				
				finalSstorage = cloudl.get(0) * sstorage;
				System.out.println("total value"+cloudl.get(0) * sstorage);
				System.out.println("storage value=="+sstorage);
				System.out.println("finalSstorage=="+finalSstorage+"sstorage1"+sstorage);
				
				gCalVo.setSstoragecost(finalSstorage);
				gCalVo.setSstorage(sstorage);
				users.get(0).setSstorage(sstorage);
				users.get(0).setSstoragecost(finalSstorage);
				}else {
					users.get(0).setSstorage(0.0);
					users.get(0).setSstoragecost(0.0);
				}
				if(!nstorage.isNaN()) {
					System.out.println("nstorage is not null"+cloudstorageregion);
					getCloudNStorageValue(cloudstorageregion , nstorage, cloud2);
					System.out.println("nstorage Value"+cloud2.get(0));
					
					finalSstorage2 = cloud2.get(0) * nstorage;
					System.out.println("total value"+cloud2.get(0) * nstorage);
					System.out.println("storage value=="+nstorage);
					System.out.println("finalSstorage=="+finalSstorage2+"sstorage1"+nstorage);
					
					
					gCalVo.setNstorage(nstorage);
					gCalVo.setNstoragecost(finalSstorage2);
					users.get(0).setNstorage(nstorage);
					users.get(0).setNstoragecost(finalSstorage2);
					}else {
						users.get(0).setNstorage(0.0); 
						 users.get(0).setNstoragecost(0.0);
					}
				if(!cstorage.isNaN()) {
					System.out.println("cstorage is not null");
					getCloudCStorageValue(cloudstorageregion , cstorage, cloud3);
					System.out.println("cstorage Value"+cloud3.get(0));
					
					finalCstorage = cloud3.get(0) * cstorage;
					System.out.println("total value"+cloud3.get(0) * cstorage);
					System.out.println("storage value=="+cstorage);
					System.out.println("finalSstorage=="+finalCstorage+"cstorage"+cstorage);
					
					gCalVo.setCstorage(cstorage);
					gCalVo.setCstoragecost(finalCstorage);
					users.get(0).setCstorage(cstorage);
					users.get(0).setCstoragecost(finalCstorage);
					}else {
						users.get(0).setCstorage(0.0); 
						 users.get(0).setCstoragecost(0.0);
					}
				if(!astorage.isNaN()) {
					System.out.println("astorage is not null");
					getCloudAStorageValue(cloudstorageregion , astorage, cloud4);
					System.out.println("astorage Value"+cloud4.get(0));
					
					finalAstorage = cloud4.get(0) * astorage;
					System.out.println("total value"+cloud4.get(0) * astorage);
					System.out.println("storage value=="+astorage);
					System.out.println("finalSstorage=="+finalAstorage+"sstorage1"+astorage);
					
					gCalVo.setAstoragecost(finalAstorage);
					gCalVo.setAstorage(astorage);
					users.get(0).setAstorage(astorage);
					users.get(0).setAstoragecost(finalAstorage);
					}else {
						users.get(0).setAstorage(0.0);
						users.get(0).setAstoragecost(0.0);
					}
				System.out.println("diskregison"+diskregion+"ssdstorage"+ssdstorage+"hssdstorage"+hssdstorage);
				if(!ssdstorage.isNaN()) {
					System.out.println("ssd is not null"+ssdstorage);
					getdiskCostSSD(diskregion , ssdstorage, cloud5);
					System.out.println("ssd Value"+cloud5.get(0));
					
					gCalVo.setSsdstorage(cloud5.get(0));
					users.get(0).setSsdstoragecost(cloud5.get(0));
					users.get(0).setSsdstorage(ssdstorage);
					}else {
						users.get(0).setSsdstorage(0.0);
						users.get(0).setSsdstoragecost(0.0);
					}
				if(!hssdstorage.isNaN()) {
					System.out.println("hssdstorage is not null"+cloudstorageregion);
					getdiskCostHSSD(diskregion , hssdstorage, cloud6);
					System.out.println("hssdstorage Value"+cloud6.get(0));
					
					gCalVo.setHssdstorage(hssdstorage);
					gCalVo.setHssdstorage(cloud6.get(0));
					users.get(0).setHssdstoragecost(cloud6.get(0));
					users.get(0).setHssdstorage(hssdstorage);
					}else {
						users.get(0).setHssdstorage(0.0);
						users.get(0).setHssdstoragecost(0.0);
					}
				if(!vpnvalue.isNaN()) {
					System.out.println("vpnvalue is not null"+vpnvalue);
					getCloudVPNValue(vpnregion , vpnvalue, vpnlist);
					System.out.println("vpnlist Value"+vpnlist.get(0));
					users.get(0).setVpnregion(vpnregion);
					users.get(0).setVpnvalue(vpnvalue);
					users.get(0).setVpncost(vpnlist.get(0));
					
					}else {
						users.get(0).setVpnregion(vpnregion);
						users.get(0).setVpnvalue(0.0);
						users.get(0).setVpncost(0.0);
					}
				/*if(frules>=1 && ingressdata>=1) {
					System.out.println("frules is not null"+frules);
					System.out.println("ingressdata is not null"+ingressdata);					
					getCloudLoadValue(loadregion , frules, cloudload);
					System.out.println("min for till 5 GB Value"+cloudload.get(0));
					System.out.println("1 GB Value"+cloudload.get(1));
					System.out.println("data Value"+cloudload.get(2));
					Double ingressCost = ingressdata * cloudload.get(2);
					Double ingressvalue = frules - 5;
					
					Double frulesCost = 0.0;
					if(ingressvalue >= 1) {
						System.out.println("ingressvalue=="+ingressvalue+"cloudload.get(1)=="+cloudload.get(1));
						fingresscost = ingressvalue * cloudload.get(1);
					}
					fingresscost = cloudload.get(0) * 730 + fingresscost * 730 +  ingressCost;
					System.out.println("final load cost=="+fingresscost+"ingressvalue"+ingressvalue+"frules"+frules);
					
					users.get(0).setLoadregion(loadregion);
					users.get(0).setLoadcost(fingresscost);
					users.get(0).setLoadvalue(frules);
					}else {
						users.get(0).setLoadvalue(0.0);
						users.get(0).setLoadregion(loadregion);
						users.get(0).setLoadcost(0.0);
					}
				if(!egreesst.isNaN() && !egreesst.isNaN()) {
					System.out.println("egreesst is not null"+egreesst);
					System.out.println("egressregion1 is not null"+egressregion1);
					System.out.println("egressregion2 is not null"+egressregion2);
					getIngressCostValue(egressregion1 , egressregion2, egreesst, cloudingress);
					System.out.println("1 GB cost"+cloudingress.get(0));
					System.out.println("10 GB cost"+cloudingress.get(1));
					System.out.println("11 GB cost"+cloudingress.get(2));
					
					Double totalcost1 = 0.0;
					Double totalvalue = 0.0;
					Double totalvalue1 = 0.0;
					Double totalvalue2 = cloudingress.get(0);
					if(egreesst > 1 && egreesst <= 10)
					{
						
						totalvalue = egreesst - 1;
						totalcostegress = totalvalue * cloudingress.get(1);
					}else if(egreesst > 10) {
						totalvalue1 = egreesst - 11;
						totalcost1 = totalvalue1 * cloudingress.get(2);
					}else if(egreesst == 0.0) {
						totalvalue2 = 0.0;
						totalcostegress = 0.0;
						totalcost1 = 0.0;
					} 
					
					totalcostegress = totalvalue2 + totalcostegress +  totalcost1;
					System.out.println("final ingress cost=="+totalcostegress+"totalcost"+totalcostegress+"totalcost"+totalcost1);					
					users.get(0).setEgressregion(egressregion1);
					users.get(0).setEgressregion1(egressregion2);
					users.get(0).setEgreesstcost(totalcostegress);
					}else {
						users.get(0).setEgressregion(egressregion1);
						users.get(0).setEgressregion1(egressregion2);
						users.get(0).setEgreesstcost(0.0);
					} */
				// interconnect cost
				Double dedicatedCost1 = 0.0;
				Double dedicatedCost2 = 0.0;
				Double dedicatedCost3 = 0.0;
				if(!dedicated1.isNaN() && !dedicated1.isNaN()) {
					dedicatedCost1 = dedicated1 * 1700;
				}
				if(!dedicated2.isNaN() && !dedicated2.isNaN()) {
					dedicatedCost2 = dedicated2 * 13000;
				}
				if(!dedicated3.isNaN() && !dedicated3.isNaN()) {
					dedicatedCost3 = dedicated3 * 73.00;
				}
				interconnectcost = dedicatedCost1 + dedicatedCost2 + dedicatedCost3;
				users.get(0).setDedicatedCost1(dedicatedCost1);
				users.get(0).setDedicatedCost2(dedicatedCost2);
				users.get(0).setDedicatedCost3(dedicatedCost3);
				users.get(0).setDedicated1(dedicated1);
				users.get(0).setDedicated2(dedicated2);
				users.get(0).setDedicated3(dedicated3);
				users.get(0).setInterconnectcost(interconnectcost);
				System.out.println("interconnectcost"+interconnectcost+"dedicated1=="+dedicated1+"dedicatedCost1=="+dedicatedCost1+"dedicated2=="+dedicated2+"dedicatedCost2=="+dedicatedCost2+"dedicatedCost3::"+dedicatedCost3);
				// partner cost
				Double partnercost = util.getPartnerCost(partnerdregion, partner);
				users.get(0).setPartnercost(partnercost);
				users.get(0).setPartnerdregion(partnerdregion);
				users.get(0).setPartner(partner);
				// firewall cost 
				Double fwcost = 0.0;
				fwcost = util.getfirwallcost(fwselction, fw, fwquantity, dr);
				fwcost = fwcost + (fwcost * 30 ) /100 ;
				System.out.println("fwselction=="+fwselction+"fw=="+fw+"fwquantity=="+fwquantity+"dr=="+dr);
				
				if(dr==null || dr.isEmpty()){
					dr = "No";
				}else if(dr.equalsIgnoreCase("1") && "1".equalsIgnoreCase(dr)) {
					 dr = "Yes"; 
				}else { 
					dr = "No"; 
				}
				users.get(0).setFwType(fw);
				users.get(0).setFwselction(fwselction);
				users.get(0).setFwquantity(fwquantity);
				users.get(0).setFirewallcost(fwcost);
				users.get(0).setDr(dr);
				//Anti-virus cost
				Double avcost = 0.0;
				if(av.equalsIgnoreCase("Symantec") && "Symantec".equalsIgnoreCase(av)) {
					avcost = avquantity * 56;
				} else if(av.equalsIgnoreCase("Trend Micro") && "Trend Micro".equalsIgnoreCase(av)) {
					avcost = avquantity * 133;
				}
				users.get(0).setAvType(av);
				users.get(0).setAvquantity(avquantity);
				users.get(0).setAnitviruscost(avcost);
				// total details
				Double TotalstandardCost = 0.0;
				Double Totalstandard1yrsCost = 0.0;
				Double Totalstandard3yrsCost = 0.0;
				TotalCost = TotalOndemandyearscost + nonProdTotalOndemandyearscost + TotalLicenseCost + nonProdTotalLicenseCost + TotalDiskCost + nProdTotalDiskCost +finalSstorage+finalCstorage+finalAstorage+finalSstorage2+cloud5.get(0)+cloud6.get(0)+vpnlist.get(0)+fingresscost+interconnectcost+fwcost+avcost+partnercost;
				TotalstandardCost = TotalOndemandinstandardcost + nonProdTotalOndemandinstandardcost + TotalLicenseinstandardCost + nonProdTotalLicenseinstandardCost + TotalDiskCost + nProdTotalDiskCost +finalSstorage+finalCstorage+finalAstorage+finalSstorage2+cloud5.get(0)+cloud6.get(0)+vpnlist.get(0)+fingresscost+interconnectcost+fwcost+avcost+partnercost;
				Double Total1yrsCostMonthly = 0.0;
				System.out.println("Totalondemand="+TotalOndemandyearscost+"L=" + TotalLicenseCost+"D=" + TotalDiskCost+"FS="+finalSstorage+"FC="+finalCstorage+"AL="+finalAstorage+"FS="+finalSstorage2+"SS="+cloud5.get(0)+"HDD="+cloud6.get(0)+"VPN="+vpnlist.get(0)+"Load="+fingresscost+"Engress="+totalcostegress);
				System.out.println("TotalCost====="+TotalCost);
				System.out.println("TotalstandardCost====="+TotalstandardCost);
				Double premiumCost = 15000.00 +  TotalCost*3/100;
				Double premiumstandardCost = 15000.00 +  TotalstandardCost*3/100;
				users.get(0).setPremiumCost(premiumCost);
				users.get(0).setPremiumstandardCost(premiumstandardCost);
				users.get(0).setTotalfinalCost(TotalCost);
				users.get(0).setTotalDemandStandardCost(TotalstandardCost);
				Total1yrsCostMonthly = Total1yearscost + nonProdTotal1yearscost + TotalLicenseCost + nonProdTotalLicenseCost + TotalDiskCost +  nProdTotalDiskCost +finalSstorage +finalCstorage +finalAstorage +finalSstorage2 +cloud5.get(0) +cloud6.get(0) +vpnlist.get(0) +fingresscost +interconnectcost +fwcost+avcost+partnercost;
				Total1yrsCost = Total1yearscost+ nonProdTotal1yearscost + TotalLicenseCost+ nonProdTotalLicenseCost  + TotalDiskCost +  nProdTotalDiskCost +finalSstorage+finalCstorage+finalAstorage+finalSstorage2+cloud5.get(0)+cloud6.get(0)+vpnlist.get(0)+fingresscost+interconnectcost+fwcost+avcost+partnercost;
				Totalstandard1yrsCost = Total1yearsinstandardcost  + nonProdTotal1yearsinstandardcost  + TotalLicenseinstandardCost  + nonProdTotalLicenseinstandardCost  + TotalDiskCost  + nProdTotalDiskCost  +finalSstorage+finalCstorage +finalAstorage +finalSstorage2 +cloud5.get(0) +cloud6.get(0) +vpnlist.get(0) +fingresscost +interconnectcost +fwcost+avcost+partnercost;
				Total3yrsCost = Total3yearscost + nonProdTotal3yearscost  +TotalLicenseCost  + nonProdTotalLicenseCost  + TotalDiskCost  + + nProdTotalDiskCost  +finalSstorage+finalCstorage +finalAstorage +finalSstorage2 +cloud5.get(0) +cloud6.get(0) +vpnlist.get(0) +fingresscost +interconnectcost +fwcost+avcost+partnercost;
				Totalstandard3yrsCost = Total3yearsinstandardcost + nonProdTotal3yearsinstandardcost + TotalLicenseinstandardCost + nonProdTotalLicenseinstandardCost + TotalDiskCost + nProdTotalDiskCost +finalSstorage+finalCstorage +finalAstorage +finalSstorage2 +cloud5.get(0) +cloud6.get(0) +vpnlist.get(0) +fingresscost +interconnectcost +fwcost+avcost+partnercost;
				Double premiumCost1 = 15000.00 +  Total1yrsCost*3/100;
				Double premiumCost1Monthly = 15000.00 +  Total1yrsCostMonthly*3/100;
				Double premiumCost3 = 15000.00 +  Total3yrsCost*3/100;
				Double premiumCoststandard1 = 15000.00 +  Totalstandard1yrsCost*3/100;
				Double premiumCoststandard3 = 15000.00 +  Totalstandard3yrsCost*3/100;
				users.get(0).setPremium1yrsCost(premiumCost1Monthly);
				users.get(0).setTotalfinal1Cost(Total1yrsCost);
				users.get(0).setPremium3yrsCost(premiumCost3);
				users.get(0).setTotalfinal3Cost(Total3yrsCost);
				users.get(0).setPremiumstandardCost1(premiumCoststandard1);
				users.get(0).setTotal1yrsStandardCost(Totalstandard1yrsCost);
				users.get(0).setPremiumstandardCost2(premiumCoststandard3);
				users.get(0).setTotal3yrsStandardCost(Totalstandard3yrsCost);
				// 3 hrs cost
				
				
				
				// deal details
				users.get(0).setDealname(dealname);
				users.get(0).setCountry(country);
				users.get(0).setCname(cname);
				users.get(0).setVertical(vertical);
				users.get(0).setDealstage(dealstage);
				users.get(0).setEmail(email);
				users.get(0).setArchitect(architect);
				
				
				 
				model.addAttribute("users", users);
				model.addAttribute("status", true);
				

			} catch (Exception ex) {
				System.out.println("Exception error ==="+ex);
				model.addAttribute("message", "An error occurred while processing the CSV file.");
				model.addAttribute("status", false);
			}
		}

		return "file-upload-status";
	}

	@PostMapping("/abc")
	public String saveNewLaunchPlan(@RequestBody Map<String, String> vo) {
		return "tested";
	}
	
	
	public void getcalRAM(String query1, String instanceType, String location, Double memory, String commitmentType, List<Double> l1)
	{
		
	System.out.println("==instanceType="+instanceType+"location="+location+"memory="+memory+"commitmentType="+commitmentType);
	if((instanceType=="Auto Select") || "Auto Select".equalsIgnoreCase(instanceType))
	{
		System.out.println("instanceType="+instanceType);
		query1 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalram from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType ='"+commitmentType+"' and categoryresourceGroup ='RAM'";
	}
	else {
				
		String getInstaceType = util.getInstanceTypeRAM(instanceType, commitmentType);
		query1 = "select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as totalram from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType ='"+commitmentType+"' and categoryresourceGroup ='RAM' and description like '%"+getInstaceType+"%'";
		
	}
	System.out.println("query=="+query1);
	try {
	jdbcTemplate.queryForObject(query1,new Object[] {location},new
			RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
					int rowNum) throws SQLException { Vo obj = new
					Vo();
			String value = rs.getString("totalram");
			Double ramcost = Double.parseDouble(value);
			System.out.println("Value1: "+value);
			Double FinalRamcost = ramcost*730*memory;
			System.out.println("Total RAM Cost =" + FinalRamcost);
			l1.add(FinalRamcost);
			return obj; 
	}
} );}catch (DataAccessException e) 
{			
	l1.add(0.0);
}

}
	public void getcalCPU(String query2, String instanceType, String location, Double vCPU, String commitmentType, List<Double> l1)
	{
		if((instanceType=="Auto Select") || "Auto Select".equalsIgnoreCase(instanceType))
		{
			 query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
			 System.out.println(query2);
		//String query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
		}
		else {
					
					String getInstaceType = util.getInstanceTypeCPU(instanceType, commitmentType);
		//}((users.get(i).getInstanceType()=="N1") || "N1".equalsIgnoreCase(users.get(i).getInstanceType()))
		//{
			query2 = "select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU' and description like '%"+getInstaceType+"%'";
			System.out.println(query2);
			
		}
		//String query2 = "select min(pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000) as totalcpu from gcp_cal where serviceRegions0 =?  and categoryresourceFamily = 'compute' and categoryusageType =? and categoryresourceGroup ='CPU'";
		try {
		jdbcTemplate.queryForObject(query2,new Object[] {location,commitmentType},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totalcpu");
				Double cpucost = Double.parseDouble(value);
				//System.out.println("query1"+query2);
				System.out.println("Value1: "+value);
				Double FinalCPUcost = cpucost*730*vCPU;							
				System.out.println("Total CPU Cost =" + FinalCPUcost);
				l1.add(FinalCPUcost);
				return obj; 
		}
} );
	}catch (DataAccessException e) 
	{			
		l1.add(0.0);
	}
		
	}
	
	public void getdiskCost(String location, String diskType, Double diskspace, List<Double> lD)
	{
		
		//String diskTypeQuery = util.getdiskTypeConDB(diskType);
		System.out.println("diskType="+diskType+"diskspace="+diskspace+"diskTypeQuery="+diskType);
		String query1 = "select diskprice as totaldisk from gcpcal_disk where location =?  and item = ?";
		System.out.println("query=="+query1);
		try {
		jdbcTemplate.queryForObject(query1,new Object[] {location,diskType},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totaldisk");
				Double diskcost = Double.parseDouble(value);
				System.out.println("diskcost: "+diskcost+"diskspace"+diskspace);
				Double Finaldiskcost = diskcost*diskspace;
				System.out.println("Total Disk Cost =" + Finaldiskcost);
				lD.add(Finaldiskcost);
				return obj; 
		}
	} ); }catch (DataAccessException e) 
	{			
		lD.add(0.0);
	}
	
	}
	
	public void getLicenseCost(String os,Double totalCPU,  List<Double> lic)
	{
		String query1 = "";
		System.out.println("os="+os+"totalCPUtotalCPU="+totalCPU);
		String skuId = util.getLicenseSKUId(os, totalCPU);
		System.out.println("skuId=="+skuId);
		query1 = "select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as totallicensecost from gcp_cal where skuId =?";
		System.out.println("query=="+query1);
		try {
		jdbcTemplate.queryForObject(query1,new Object[] {skuId},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totallicensecost");
				Double totallicensecost = Double.parseDouble(value);
				lic.clear();
				lic.add(totallicensecost);
				return obj; 
		}
	} ); }catch (DataAccessException e) 
	{		
		lic.clear();
		lic.add(0.0);
	}
	
	}
	
	public void getcalRAMCommitExtendDCustom(String query2, String instanceType, String location, String instance, String item, Double vCPU, String commitmentType, List<Double> ECD)
	{
		location = util.getlocation(location);		
		String getInstaceType = util.getInstanceExtendRAM(instanceType);
		System.out.println("==getInstaceType"+getInstaceType+"location=="+location);
		query2 = "select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as totalextended from gcp_cal where serviceRegions0 =? and categoryresourceFamily = 'Compute' and categoryresourceGroup = 'RAM' and categoryusageType = 'OnDemand' and description like '"+getInstaceType+"%'";
		
		System.out.println(query2);
		try {
		jdbcTemplate.queryForObject(query2,new Object[] {location},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totalextended");
				System.out.println("extended disk cost =="+value);
				Double cpucost = Double.parseDouble(value);
				ECD.clear();
				ECD.add(cpucost);
				return obj; 
		}
} 	);}catch (DataAccessException e) 
		{
	ECD.clear();
	ECD.add(0.0);
}
		
	}
	
	
	public void getm2Mappingcost(String location, String description, String commitmentType, List<Double> M2L)
	{
		     String query3 = "";
		     query3 = "select * from m2instance where location = ? and description = ? and commitmentType= ?";
			 System.out.println(query3);
		
			try {
			jdbcTemplate.queryForObject(query3,new Object[] {location,description,commitmentType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();					
						String price = rs.getString("price");
						Double price1 = Double.parseDouble(price);
						M2L.add(price1);
					
					return obj; 
			}
			} );}catch (DataAccessException e) 
					{			
				M2L.add(0.0);
				
			}
		
	}
	
	public void getInstanceMapping(String applicationType, Double vCPU, Double memory, String instanceType, List<String> lM)
	{ 
		
	 String query = "";
    
     final String value = "No Instance Found";
     System.out.println("vCPU"+vCPU+"memory"+memory);
	 query = "select skuName,saps from sku_mapping where cpu="+vCPU+" and ram="+memory+" and sapdetail=? and instancetype='"+instanceType+"'";
	 System.out.println(query);

	try {
	jdbcTemplate.queryForObject(query,new Object[] {applicationType},new
			RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
					int rowNum) throws SQLException { Vo obj = new
					Vo();
			 String value = rs.getString("skuName");
			 String valuesaps = rs.getString("saps");
			 System.out.println("skuName=="+value+"valuesaps=="+valuesaps);
			 lM.clear();
			 lM.add(value);
			 lM.add(valuesaps);
			return obj; 
	}
	} );}catch (DataAccessException e) 
			{
		 lM.clear();
		 lM.add(value);
		 lM.add("0.0");
	}		
	}
	
	public HashMap<String, Double> getMinimumCostSInstance(String location, String instance, String applicationType,String CType, Double vCPU, Double memory, String instanceType)
	{
			
			HashMap<String, Double> hm = new HashMap<String, Double>(); 
			String query = "select price as price,CONCAT(instanceType,'-',item) as instanceType from instancedetails where location = ? and instance = ? and commitmentType= ? and price !=0 and instanceType IN (select instancetype from sku_mapping where cpu = ? and ram = ? and sapdetail = ? and instanceType in ("+instanceType+") ) order by price asc";
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance,CType,vCPU,memory,applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("instanceType");
							hm.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("instanceType");	
								hm.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			hm.put("1", 0.0);
			hm.put("2", 0.0);
		}
			return hm;		
	}
	
	public HashMap<String, Double> getM2Cost(String location, String instance)
	{
			
			HashMap<String, Double> hm = new HashMap<String, Double>(); 
			String query = "select price as price,commitmentType as commitmentType from m2instance where location = ? and description = ?";
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("commitmentType");
							hm.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("commitmentType");	
								hm.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			hm.put("1", 0.0);
			hm.put("2", 0.0);
		}
			return hm;		
	}
	
	
	public HashMap<String, Double> getMinimumCostSNextInstance(String location, String instance, String applicationType,String CType, Double vCPU, Double memory, String instanceType)
	{
			HashMap<String, Double> hm = new HashMap<String, Double>(); 
			String query = "select price as price,CONCAT(instanceType,'-',item) as instanceType from instancedetails where location = ? and instance = ? and commitmentType= ? and price !=0 and instanceType IN (select instancetype from sku_mapping where cpu >= ? and ram >= ? and sapdetail = ? and instanceType in ("+instanceType+")) order by price asc";
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance,CType,vCPU,memory,applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("instanceType");
							hm.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("instanceType");	
								hm.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			hm.put("1", 0.0);
			hm.put("2", 0.0);
		}
			return hm;		
	}
	
	public HashMap<String, Double> getMinimumCostIncreasePerInstance(String location, String instance, String applicationType,String CType, Double minCPU, Double maxCPU,Double minmemory, Double maxmemory, String instanceType)
	{
			HashMap<String, Double> hm = new HashMap<String, Double>(); 
			String query = "select price as price,CONCAT(instanceType,'-',item) as instanceType from instancedetails where location = ? and instance = ? and commitmentType= ? and price !=0 and instanceType IN (select distinct(instancetype) from sku_mapping where CPU BETWEEN ? AND ? and ram between ? AND ? and sapdetail = ? and instanceType in ("+instanceType+") ) order by price asc";
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance,CType,minCPU,maxCPU,minmemory,maxmemory,applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("instanceType");
							hm.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("instanceType");	
								hm.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			hm.put("1", 0.0);
			hm.put("2", 0.0);
		}
			return hm;		
	}
	
	public HashMap<String, Double> getMinimumCostStdInstance(String location, String instance, String instanceType)
	{
			HashMap<String, Double> pricevalue = new HashMap<String, Double>(); 
			String query = "select price as price,CONCAT(commitmentType,'-',item) as instanceType from instancedetails where location = ? and instance = ? and price !=0 and instanceType = ? order by price asc";
								
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance,instanceType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("instanceType");
							pricevalue.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("instanceType");	
								pricevalue.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			pricevalue.put("1", 0.0);
			pricevalue.put("2", 0.0);
		}
			return pricevalue;		
	}
	
	public HashMap<String, Double> getMinimumCostCInstance(String location, String instance, String applicationType, String instanceType)
	{							   
			HashMap<String, Double> cc = new HashMap<String, Double>(); 
			String query = "select price as price,CONCAT(instanceType,'-',item,'-',commitmentType) as instanceType from instancedetails where location = ?  and instance = ? and price !=0 and instanceType IN ("+instanceType+") order by price asc";
			
			System.out.println(query);
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location,instance},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
							
							String price = rs.getString("price");
							String value = rs.getString("instanceType");
							cc.put(value,Double.parseDouble(price));
							System.out.println("price=="+price+"value=="+value);
							while(rs.next()){
								String price1 = rs.getString("price");
								String value1 = rs.getString("instanceType");	
								cc.put(value1,Double.parseDouble(price1));
					        }				       
					return obj; 
			}
		} );}catch (DataAccessException e) 
		{	
			cc.put("1", 0.0);
			cc.put("2", 0.0);
		}
			return cc;		
	}
	
	public void getInstanceMappingwithDetails(String applicationType, Double vCPU, Double memory, String instanceType, List<String> lUD)
	{
		
		//C2, M1, M2 - No custom instances - next highest

		//N1,N2,N2D,E2 - Custom 

		     String query = "";
		     final String value = "No Instance Found";
		     Double minvCPU = vCPU - vCPU*12/100;
		     Double maxvCPU = vCPU + vCPU*50/100;
		     Double minmemory = memory - memory*20/100;
		     Double maxmemory = memory + memory*40/100;
		     System.out.println("minvCPU"+minvCPU+"maxvCPU"+maxvCPU+"minmemory"+minmemory+"maxmemory"+maxmemory);
			 query = "select * from sku_mapping where sapdetail=? and CPU BETWEEN "+minvCPU+" AND "+maxvCPU+" and ram between "+minmemory+" AND "+maxmemory+" and instancetype like '"+instanceType+"%' order by cpu asc limit 0,1";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
					 String value = rs.getString("skuName");
					 String valuecpu = rs.getString("cpu");
					 String valueram = rs.getString("ram");
					 System.out.println("skuName"+value+"valuecpu"+valuecpu+"valueram"+valueram);
					 lUD.clear();
					 lUD.add(value);
					 lUD.add(valuecpu);
					 lUD.add(valueram);
					 
					return obj; 
			}
			} );}catch (DataAccessException e) 
				{
				lUD.clear();
				lUD.add("No Record");
				lUD.add("0");
				lUD.add("0");
				
			}
		
	}
	public void getInstanceMappingwithDetailscpu(String applicationType, Double vCPU, Double memory, String instanceType, List<String> lUD1)
	{
		
		//C2, M1, M2 - No custom instances - next highest

		//N1,N2,N2D,E2 - Custom 
		     String query = "";
		     final String value = "No Instance Found";
		     Double minvCPU = vCPU - vCPU*12/100;
		     Double maxvCPU = vCPU + vCPU*50/100;
		     Double minmemory = memory - memory*20/100;
		     Double maxmemory = memory + memory*40/100;
		     if((instanceType.equalsIgnoreCase("Auto Select") && "Auto Select".equalsIgnoreCase(instanceType))) {
		    	 if(!applicationType.equalsIgnoreCase("N")){
		    		 instanceType = "'E1','N2D','C1','N1','N2','M1','M2','C1'";
		    	 }else {
		    		 instanceType = "'E1','N2D','C1','N1','N2'";
		    	 }
			 }
		     else {
		    	 instanceType = "'"+instanceType+"'";
		     }
		     System.out.println("minvCPU"+minvCPU+"maxvCPU"+maxvCPU+"minmemory"+minmemory+"maxmemory"+maxmemory);
			 query = "select * from sku_mapping where sapdetail=? and cpu >="+vCPU+" and ram >="+memory+" and instancetype in ("+instanceType+") limit 0,1";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
					 String value = rs.getString("skuName");
					 String valuecpu = rs.getString("cpu");
					 String valueram = rs.getString("ram");
					 String instancetype = rs.getString("instancetype");
					 
					 System.out.println("skuName"+value+"valuecpu"+valuecpu+"valueram"+valueram);
					 lUD1.clear();
					 lUD1.add(value);
					 lUD1.add(valuecpu);
					 lUD1.add(valueram);
					 lUD1.add(instancetype);
					 
					return obj; 
			}
			} );}catch (DataAccessException e) 
					{
				lUD1.clear();
				lUD1.add("No Record");
				lUD1.add("0");
				lUD1.add("0");
				lUD1.add("No Instance Found");
				
			}
		
	}
	
	public void getInstanceMappingDetailscpu(String applicationType, Double vCPU, Double memory, String instanceType, List<String> lUD1)
	{
		
		//C2, M1, M2 - No custom instances - next highest

		//N1,N2,N2D,E2 - Custom 
		     String query = "";
		     final String value = "No Instance Found";
			 query = "select * from sku_mapping where sapdetail=? and cpu >="+vCPU+" and ram >="+memory+" and instancetype in ('"+instanceType+"') limit 0,1";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {applicationType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
					 String value = rs.getString("skuName");
					 String valuecpu = rs.getString("cpu");
					 String valueram = rs.getString("ram");					 
					 String instancetype = rs.getString("instancetype");
					 String valuesaps = rs.getString("saps");
					 
					 System.out.println("skuName"+value+"valuecpu"+valuecpu+"valueram"+valueram+"valuesaps=="+valuesaps);
					 lUD1.clear();
					 lUD1.add(value);
					 lUD1.add(valuecpu);
					 lUD1.add(valueram);					 
					 lUD1.add(instancetype);
					 lUD1.add(valuesaps);
					 
					return obj; 
			}
			} );}catch (DataAccessException e) 
					{
				lUD1.clear();
				lUD1.add("No Record");
				lUD1.add("0");
				lUD1.add("0");				
				lUD1.add("No Instance Found");
				lUD1.add("0");
				
			}
		
	}
	public void getCloudStorageValue(String location, Double StorageValue, List<Double> cloudl)
	{
		     String query = "";
			 query = "select price as price from cloudstorage where region = ? and category ='StandardStorage'";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
						
					 String value = rs.getString("price");
					 System.out.println("price"+value);
					 cloudl.add(Double.parseDouble(value));
					 return obj; 
			}
			} );}catch (DataAccessException e) 
					{			
				cloudl.add(0.0);
			}
		
	}
	public void getCloudNStorageValue(String location, Double StorageValue, List<Double> cloud2)
	{
		     String query = "";
		     System.out.println("location=="+location);
			 query = "select price as price from cloudstorage where region = ? and category ='NearlineStorage'";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
						
					 String value = rs.getString("price");
					 System.out.println("price"+value);
					 cloud2.add(Double.parseDouble(value));
					 return obj; 
			}
			} );}catch (DataAccessException e) 
					{		
				System.out.println("exception"+e);
				cloud2.add(0.0);
			}
		
	}
	
	public void getCloudCStorageValue(String location, Double StorageValue, List<Double> cloud3)
	{
		     String query = "";
		     System.out.println("location=="+location);
		     String cat = "ColdlineStorage";
		     query = "select price as price from cloudstorage where region = ? and category ='ColdlineStorage'";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
						
					 String value = rs.getString("price");
					 System.out.println("price"+value);
					 cloud3.add(Double.parseDouble(value));
					 return obj; 
			}
			} );}catch (DataAccessException e) 
					{	
				System.out.println("Exception"+e);
				cloud3.add(0.0);
			}
		
	}
	public void getCloudAStorageValue(String location, Double StorageValue, List<Double> cloud4)
	{
		     String query = "";
		     System.out.println("location=="+location);
		     String cat = "ArchiveStorage";
		     query = "select price as price from cloudstorage where region = ? and category ='ArchiveStorage'";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {location},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
						
					 String value = rs.getString("price");
					 System.out.println("price"+value);
					 cloud4.add(Double.parseDouble(value));
					 return obj; 
			}
			} );}catch (DataAccessException e) 
					{
				System.out.println("Exception"+e);
				cloud4.add(0.0);
			}
		
	}
	public void getdiskCostSSD(String location, Double diskspace, List<Double> cloud5)
	{
		
		String query1 = "select diskprice as totalssddisk from gcpcal_disk where location =?  and item = 'SSD provisioned space'";
		System.out.println("query=="+query1);
		try {
		jdbcTemplate.queryForObject(query1,new Object[] {location},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totalssddisk");
				Double diskcost = Double.parseDouble(value);
				System.out.println("diskcost: "+diskcost+"diskspace"+diskspace);
				Double Finaldiskcost = diskcost*diskspace;
				System.out.println("Total Disk Cost =" + Finaldiskcost);
				cloud5.add(Finaldiskcost);
				return obj; 
		}
	} ); }catch (DataAccessException e) 
	{			
		cloud5.add(0.0);
	}
	
	}
	public void getdiskCostHSSD(String location, Double diskspace, List<Double> cloud6)
	{
		
		String query1 = "select diskprice as totalssddisk from gcpcal_disk where location =?  and item = 'Standard provisioned space'";
		System.out.println("query=="+query1);
		try {
		jdbcTemplate.queryForObject(query1,new Object[] {location},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("totalssddisk");
				Double diskcost = Double.parseDouble(value);
				System.out.println("diskcost: "+diskcost+"diskspace"+diskspace);
				Double Finaldiskcost = diskcost*diskspace;
				System.out.println("Total Disk Cost =" + Finaldiskcost);
				cloud6.add(Finaldiskcost);
				return obj; 
		}
	} ); }catch (DataAccessException e) 
	{			
		cloud6.add(0.0);
	}
	
	}
	public void getCloudVPNValue(String location, Double vpnvalue, List<Double> cloudvpn)
	{
		
		String query1 = "select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as vpnprice from gcp_cal where categoryresourceFamily='Network' and categoryresourceGroup='VPNTunnel' and serviceRegions0 = ?";
		System.out.println("query=="+query1);
		try {
		jdbcTemplate.queryForObject(query1,new Object[] {location},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
				String value = rs.getString("vpnprice");
				Double vpncost = Double.parseDouble(value);
				System.out.println("diskcost: "+vpncost+"diskspace"+vpnvalue);
				Double Finalvpncost = vpncost*vpnvalue *730;
				System.out.println("Total VPN Cost =" + Finalvpncost);
				cloudvpn.add(Finalvpncost);
				return obj; 
		}
	} ); }catch (DataAccessException e) 
	{			
		cloudvpn.add(0.0);
	}
	
  }
	public void getCloudLoadValue(String loadregion, Double frules, List<Double> cloudload)
	{
		  String query1 ="select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as loadprice from gcp_cal where serviceRegions0 = ? and categoryresourceFamily = 'Network' and categoryresourceGroup = 'LoadBalancing' and categoryserviceDisplayName = 'Compute Engine' and description like 'Network Load Balancing: Forwarding Rule Minimum Service Charge%'";
		  System.out.println("query=="+query1); 
		  
		  try {
				jdbcTemplate.queryForObject(query1,new Object[] {loadregion},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("loadprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("loadmincost"+loadmincost);
						System.out.println("Total loadmincost =" + loadmincost);
						cloudload.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudload.add(0.0);
			}
		  String query2 ="select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as maxloadprice from gcp_cal where serviceRegions0 = ? and categoryresourceFamily = 'Network' and categoryresourceGroup = 'LoadBalancing' and categoryserviceDisplayName = 'Compute Engine' and description like 'Network Load Balancing: Forwarding Rule Additional Service Charge%'";
		  System.out.println("query=="+query2); 
		  
		  try {
				jdbcTemplate.queryForObject(query2,new Object[] {loadregion},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("maxloadprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("maxloadprice"+loadmincost);
						System.out.println("Total maxloadprice =" + loadmincost);
						cloudload.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudload.add(0.0);
			}
		  String query3 ="select pricingInfo0pricingExpressiontieredRates0unitPricenanos/1000000000 as dataloadprice from gcp_cal where serviceRegions0 = ? and categoryresourceFamily = 'Network' and categoryresourceGroup = 'LoadBalancing' and categoryserviceDisplayName = 'Compute Engine' and description like 'Network Load Balancing: Data Processing Charge%'";
		  System.out.println("query=="+query3); 
		  try {
				jdbcTemplate.queryForObject(query3,new Object[] {loadregion},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("dataloadprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("dataloadprice"+loadmincost);
						System.out.println("Total dataloadprice =" + loadmincost);
						cloudload.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudload.add(0.0);
			}
}	
	public void getIngressCostValue(String egressregion1, String egressregion2, Double egreesst, List<Double> cloudingress)
	{
		  String query1 ="select price as miniprice from gcp_cal_ingress where region =? and regionbelong = ? and size ='1'";
		  System.out.println("query=="+query1); 
		  
		  try {
				jdbcTemplate.queryForObject(query1,new Object[] {egressregion1,egressregion2},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("miniprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("miniprice"+loadmincost);
						System.out.println("Total ingressmincost =" + loadmincost);
						cloudingress.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudingress.add(0.0);
			}
		  String query2 ="select price as tenprice from gcp_cal_ingress where region =? and regionbelong = ? and size ='10'";
		  System.out.println("query=="+query2); 
		  
		  try {
				jdbcTemplate.queryForObject(query2,new Object[] {egressregion1,egressregion2},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("tenprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("tenprice"+loadmincost);
						System.out.println("Total tenprice =" + loadmincost);
						cloudingress.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudingress.add(0.0);
			}
		  String query3 ="select price as elevenprice from gcp_cal_ingress where region =? and regionbelong = ? and size ='11'";
		  System.out.println("query=="+query3); 
		  try {
				jdbcTemplate.queryForObject(query3,new Object[] {egressregion1,egressregion2},new
						RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
								int rowNum) throws SQLException { Vo obj = new
								Vo();
						String value = rs.getString("elevenprice");
						Double loadmincost = Double.parseDouble(value);
						System.out.println("elevenprice"+loadmincost);
						System.out.println("Total elevenprice =" + loadmincost);
						cloudingress.add(loadmincost);
						return obj; 
				}
			} ); }catch (DataAccessException e) 
			{			
				cloudingress.add(0.0);
			}
}	
	public void getcalAll(String query, String instanceType, String location, String instance, List<Double> COM)
	{
		
			query = "select price as price from instancedetails where location = ? and instanceType = '"+instanceType+"' and instance='standard'";
			System.out.println(query);
			COM.clear();
			
		try {
		jdbcTemplate.queryForObject(query,new Object[] {location},new
				RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
						int rowNum) throws SQLException { Vo obj = new
						Vo();
						
						String value = rs.getString("price");
						Double cpucost = Double.parseDouble(value);
						System.out.println("cpucost=="+cpucost);
						COM.add(cpucost);
						while(rs.next()){
							String value1 = rs.getString("price");
							Double cpucost1 = Double.parseDouble(value1);
							System.out.println("cpucost=="+cpucost1);
							COM.add(cpucost1);
				        }				       
				return obj; 
		}
		} );}catch (DataAccessException e) 
		{	
			COM.clear();
			COM.add(0.0);
			COM.add(0.0);
			COM.add(0.0);
			COM.add(0.0);
			COM.add(0.0);
			COM.add(0.0);
		}
		
	}
	
	public Double getLicenseCostFinalCost(String os, Double totalCPU, Double hrs, List<Double> lic) {
		Double finaLCost = 0.0;
		
		if(("".equalsIgnoreCase(os) && os.equalsIgnoreCase("")) || (totalCPU == 99999999.0)) {
			finaLCost = 0.0;
		}else {
		getLicenseCost(os, totalCPU, lic);
		Double licenseCost = lic.get(0);				
		Double licenseValue = util.getLicenseFinalCost(os, totalCPU, licenseCost, hrs);
		System.out.println("Final License cost "+licenseValue);
		return licenseValue;
		}		
		return finaLCost;		
	}
	
	public void getInstanceMappingDetailsStandardcpu(String applicationType, Double minvCPU, Double maxvCPU, Double minmemory, Double maxmemory, String instanceType, List<String> lUD1)
	{
		
		//C2, M1, M2 - No custom instances - next highest

		//N1,N2,N2D,E2 - Custom 
		     String query = "";
		     final String value = "No Instance Found";
			 query = "select * from sku_mapping where sapdetail=? and CPU BETWEEN "+minvCPU+" AND "+maxvCPU+" and ram between "+minmemory+" AND "+maxmemory+" and instancetype = ? order by cpu asc limit 0,1";
			 System.out.println(query);
		
			try {
			jdbcTemplate.queryForObject(query,new Object[] {applicationType,instanceType},new
					RowMapper<Vo>(){ public Vo mapRow(ResultSet rs,
							int rowNum) throws SQLException { Vo obj = new
							Vo();
					 String value = rs.getString("skuName");
					 String valuecpu = rs.getString("cpu");
					 String valueram = rs.getString("ram");
					 String instancetype = rs.getString("instancetype");
					 String saps = rs.getString("saps");
					 
					 System.out.println("skuName"+value+"valuecpu"+valuecpu+"valueram"+valueram);
					 lUD1.clear();
					 lUD1.add(value);
					 lUD1.add(valuecpu);
					 lUD1.add(valueram);
					 lUD1.add(instancetype);
					 lUD1.add(saps);
					 
					return obj; 
			}
			} );}catch (DataAccessException e) 
					{
				lUD1.clear();
				lUD1.add("No Instance Found");
				lUD1.add("0");
				lUD1.add("0");
				lUD1.add("No Instance Found");
				lUD1.add("0");
				
			}
		
	}
	
	public HashMap<String, Double> calculateAllCustomCost(HashMap<String, Double> costvalue, Double hrs, Double totalCPU,Double memory, String applicationType, String instanceType,Double extendedDisk, String location)
	{
			String query1 = "";
			List <Double> ECD = new ArrayList<>();
			HashMap<String, Double> CustomValue = new HashMap<String, Double>(); 
			Double yr3cpu = costvalue.get(""+instanceType+"-Custom vCPUs-3year");
			Double yr3memory = costvalue.get(""+instanceType+"-Custom Memory-3year");
			Double yr1cpu = costvalue.get(""+instanceType+"-Custom vCPUs-1year");
			Double yr1memory = costvalue.get(""+instanceType+"-Custom Memory-1year");
			Double ondemadcpu = costvalue.get(""+instanceType+"-Custom vCPUs-Ondemand");
			Double ondemadmemory = costvalue.get(""+instanceType+"-Custom Memory-Ondemand");
			Double ExtendedDiskCost = 0.0;
			Double ExtendedDiskCostAll = 0.0;
			if(extendedDisk > 1.0) {
				getcalRAMCommitExtendDCustom(query1,instanceType,location, "extended custom", "Extended custom memory", extendedDisk, "extended custom", ECD);
				ExtendedDiskCost = extendedDisk * hrs * ECD.get(0);
				ExtendedDiskCostAll = extendedDisk * 730 * ECD.get(0);
				System.out.println("ExtendedDisk"+extendedDisk+"ECD.get(0)::"+ECD.get(0)+"hrs::"+hrs);
				/*
				 * discountValue = util.getDiscountValue(instanceType, ExtendedDiskCost, hrs);
				 * ExtendedDiskCost = ExtendedDiskCost - discountValue; //ExtendedDiskCost =
				 * 161.04;
				 */			
				System.out.println("extended cost"+ExtendedDiskCost);				
			}
			System.out.println("yr3cpu=="+yr3cpu+"yr3memory"+yr3memory+"yr1cpu"+yr1cpu+"yr1memory"+yr1memory+"ondemadcpu"+ondemadcpu+"ondemadmemory"+ondemadmemory);
			Double FinalCPUcost = ondemadcpu*hrs*totalCPU;
			Double FinalRAMcost = ondemadmemory*hrs*memory;					
		    Double finalValueOndemad = 0.0;	
			finalValueOndemad = FinalCPUcost+FinalRAMcost+ExtendedDiskCost; 
			System.out.println("Total CPU Ondemand Cost =" + FinalCPUcost+"RAM cost"+FinalRAMcost+"Total cost"+finalValueOndemad);
			Double discountValue = util.getDiscountValue(instanceType, finalValueOndemad, hrs);
		    finalValueOndemad = finalValueOndemad - discountValue;
		    System.out.println("finalValueOndemad=="+finalValueOndemad+"discountValue=="+discountValue);
			Double Final1yrsCPUcost = yr1cpu*730*totalCPU;
			Double Final1yrsRAMcost = yr1memory*730*memory;					
		    Double finalValue1yrs = 0.0;	
		    finalValue1yrs = Final1yrsCPUcost+Final1yrsRAMcost+ExtendedDiskCost; 
			System.out.println("Total CPU 1 year Cost =" + Final1yrsCPUcost+"RAM cost"+Final1yrsRAMcost+"Total cost"+finalValue1yrs);
			Double finalValue3yrs = 0.0;
			Double Final3yrsCPUcost = yr3cpu*730*totalCPU;
			Double Final3yrsRAMcost = yr3memory*730*memory;	
		    finalValue3yrs = Final3yrsCPUcost+Final3yrsRAMcost+ExtendedDiskCost; 
			System.out.println("Total CPU 3 year  Cost =" + Final3yrsCPUcost+"RAM cost"+Final3yrsRAMcost+"Total cost"+finalValue3yrs);
			CustomValue.put(""+instanceType+"cOndemad", finalValueOndemad);
			CustomValue.put(""+instanceType+"c1yrs", finalValue1yrs);
			CustomValue.put(""+instanceType+"c3yrs", finalValue3yrs);								
			return CustomValue;		
	}
	
}