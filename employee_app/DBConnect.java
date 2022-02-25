package HotelMgmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnect {
	
	Connection c = null;
	
	public DBConnect(String user, String pass) {
		//ENTER YOUR UOZONE USER AND PASS
		String user_id = user;
		String password = pass;
		 try {
			 Class.forName("org.postgresql.Driver");
		     this.c = DriverManager.getConnection("jdbc:postgresql://web0.eecs.uottawa.ca:15432/group_a02_g49",user_id, password);
		 } catch (Exception e) {
			 e.printStackTrace();
		     System.err.println(e.getClass().getName()+": "+e.getMessage());
		     System.exit(0);
		 }
		 System.out.println("Opened database successfully");
		 
		 try {
			Statement stmt = null;
			 stmt = c.createStatement();
		     String sql = "SET search_path = 'public'";
		     stmt.executeUpdate(sql);
		 } catch (Exception e) {
		        e.printStackTrace();
		        System.err.println(e.getClass().getName()+": "+e.getMessage());
		        System.exit(0);
		 }
		 
	}
	
	public void closeDB() {
		try {
			this.c.close();
			System.out.println("Closed database succesfully");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
	}
	
	public int getBrandID(int sin) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery("select brand_id from public.employee where sin_number like '"+sin+"'");
		rs.next();
		int result = rs.getInt("brand_id");
		rs.close();
	    stmt.close();
		return result;
	}
	
	public int getFranchiseID(int sin) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery("select franchise_id from public.employee where sin_number= '"+sin+"'");
		rs.next();
		int result = rs.getInt("franchise_id");
		rs.close();
	    stmt.close();
		return result;
	}
	
	public void QueryRooms(int priceMin, int priceMax, String capacity, String view, String arrival_date, String departure_date) {
		try {
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( "select * from public.room WHERE price >= "+priceMin+" AND price <= "+priceMax+" AND capacity LIKE '"+capacity+"' AND view LIKE '"+view+"';" );
		    System.out.println("***********");
		    System.out.println("Displaying rooms that are between "+priceMin+"$ and "+priceMax+"$ with capacity "+capacity+ " and view "+view );
		    System.out.println();
		    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", "BRAND NAME", "BRAND ID", "FRANCHISE ID", "ROOM NUMBER", "PRICE", "CAPACITY", "EXTENDABLE", "VIEW");
	        System.out.println();
	        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	         
		    while ( rs.next() ) {
		    	 int brand_id = rs.getInt("brand_id");
		         int child_id = rs.getInt("franchise_id");
		         int room_number  = rs.getInt("room_number");
		         int price = rs.getInt("price");
		         String rcapacity = rs.getString("capacity");
		         boolean extendable = rs.getBoolean("extendable");
		         String rview = rs.getString("view");
		         if (isAvailable(brand_id, child_id, room_number, arrival_date, departure_date)) {
		        	 System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", brandidToName(brand_id), brand_id, child_id, room_number, price, rcapacity, extendable, rview);
		        	 System.out.println();
						//System.out.printf( "Brand Name = "+brandidToName(brand_id)+", BrandID = "+brand_id+" , FranchiseID = "+child_id+" , RoomNumber = "+room_number+" , Price = "+price+", Capacity = "+rcapacity+" , Extendable = "+extendable
				        //		 +" , View = " +rview);
				        // System.out.println();
					}
		      }
		      rs.close();
		      stmt.close();
		      System.out.println("***********");
		      
		}  catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
	    }
	}
	
	public void QueryRoomsWithoutDate(int priceMin, int priceMax, String capacity, String view) {
		try {
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( "select * from public.room WHERE price >= "+priceMin+" AND price <= "+priceMax+" AND capacity LIKE '"+capacity+"' AND view LIKE '"+view+"';" );
		    System.out.println("***********");
		    System.out.println("Displaying rooms that are between "+priceMin+"$ and "+priceMax+"$ with capacity "+capacity+ " and view "+view );
		    System.out.println();
		    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	        System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", "BRAND NAME", "BRAND ID", "FRANCHISE ID", "ROOM NUMBER", "PRICE", "CAPACITY", "EXTENDABLE", "VIEW");
	        System.out.println();
	        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	         
		    while ( rs.next() ) {
		    	 int brand_id = rs.getInt("brand_id");
		         int child_id = rs.getInt("franchise_id");
		         int room_number  = rs.getInt("room_number");
		         int price = rs.getInt("price");
		         String rcapacity = rs.getString("capacity");
		         boolean extendable = rs.getBoolean("extendable");
		         String rview = rs.getString("view");
		         System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", brandidToName(brand_id), brand_id, child_id, room_number, price, rcapacity, extendable, rview);
	        	 System.out.println();
		         //System.out.printf( "Brand Name = "+brandidToName(brand_id)+", BrandID = "+brand_id+" , FranchiseID = "+child_id+" , RoomNumber = "+room_number+" , Price = "+price+", Capacity = "+rcapacity+" , Extendable = "+extendable
				 //+" , View = " +rview);
				 //System.out.println();
		      }
		      rs.close();
		      stmt.close();
		      System.out.println("***********");
		      
		}  catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
	    }
	}
	
	public void FindBookings(int brand_id, int franchise_id, int room_number) throws SQLException{
		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery("select * from public.booking where brand_id = '"+brand_id+"' and franchise_id = '"+franchise_id+"' and room_number= '"+room_number+"'");
		System.out.println("Here are the bookings for room "+room_number+":");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s", "BOOKING NUMBER", "CUSTOMER SIN", "RENTING NUMBER", "ARRIVAL DATE", "DEPARTURE DATE", "ARCHIVED?");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
		while(rs.next()) {
			int booking_number = rs.getInt("booking_number");
			int customer_sin = rs.getInt("customer_sin");
			int renting_number = rs.getInt("renting_number");
			String arrival_date = rs.getString("arrival_date");
			String departure_date = rs.getString("departure_date");
			boolean archived = rs.getBoolean("archived");
			System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s", booking_number, customer_sin, renting_number, arrival_date, departure_date, archived);
	        System.out.println();
			//System.out.println( "Booking number = "+booking_number+", Customer SIN = "+customer_sin+" , renting_number = "+renting_number+" , Arrival date = "+arrival_date+" , Departure date = "+departure_date+", Archived? = "+archived);
		}
		rs.close();
		System.out.println();
		ResultSet rs2 = stmt.executeQuery("select * from public.renting where brand_id = '"+brand_id+"' and franchise_id = '"+franchise_id+"' and room_number= '"+room_number+"'");
		System.out.println("Here are the rentings for room "+room_number+":");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s", "RENTING NUMBER", "CUSTOMER SIN", "BOOKING NUMBER", "ARRIVAL DATE", "DEPARTURE DATE", "ARCHIVED?", "PAYMENT");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		while(rs2.next()) {
			int renting_number = rs2.getInt("renting_number");
			int customer_sin = rs2.getInt("customer_sin");
			int booking_number = rs2.getInt("booking_number");
			String arrival_date = rs2.getString("arrival_date");
			String departure_date = rs2.getString("departure_date");
			boolean archived = rs2.getBoolean("archived");
			boolean payment = rs2.getBoolean("payment");
			System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s", renting_number, customer_sin, booking_number, arrival_date, departure_date, archived, payment);
	        System.out.println();
			//System.out.println( "Renting number = "+renting_number+", Customer SIN = "+customer_sin+" , booking_number = "+booking_number+" , Arrival date = "+arrival_date+" , Departure date = "+departure_date+", Archived? = "+archived+", Payment? = "+payment);
		}
	    stmt.close();
	}
	
	public void ModifySpecs(int brand_id, int franchise_id, int room_number, String newPriceS, String newCapacity, String newExtendable, String newView) throws SQLException {
		String query = "update room set ";
		int newPrice = 0;
		try {
			newPrice = Integer.parseInt(newPriceS);
		}catch(Exception e) {}
		
		if(newPrice != 0) {
			query += "price = "+newPrice;
		}
		if(!newCapacity.equals("")) {
			if(newPrice == 0) {
				query += "capacity = '"+newCapacity+"'";
			}else {
				query += ", capacity = '"+newCapacity+"'";
			}
		}
		newExtendable = newExtendable.toLowerCase();
		if(newExtendable.equals("true") || newExtendable.equals("false")) {
			if(newPrice == 0 && newCapacity.equals("")) {
				query += "extendable = "+newExtendable;
			}else {
				query += ", extendable = "+newExtendable;
			}
		}
		if(!newView.equals("")) {
			if(newPrice == 0 && newCapacity.equals("") && newExtendable.equals("")) {
				query += "view = '"+newView+"'";
			}else {
				query += ", view = '"+newView+"'";
			}
		}
		if(!(newPrice == 0 && newCapacity.equals("") && newExtendable.equals("") && newView.equals(""))) {
			query += " where brand_id = " +brand_id+" and franchise_id = "+franchise_id+" and room_number = "+room_number;
			
			Statement stmt = null;
			stmt = this.c.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("***********");
		    System.out.println("Update Succesful!");
		    System.out.println("***********");
		}else{
			System.out.println("***********");
		    System.out.println("No change required");
		    System.out.println("***********");
		}
	}
	
	public void AddAmenity(int brand_id, int franchise_id, int room_number, String newAmenity) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		stmt.executeUpdate("insert into inst_amenity (brand_id, franchise_id, room_number, amenity) values ("+brand_id+", "+franchise_id+", "+room_number+", '"+newAmenity+"')");
		stmt.close();
	}
	
	public void DeleteAmenity(int brand_id, int franchise_id, int room_number, String deleteAmenity) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		stmt.executeUpdate("delete from inst_amenity where brand_id = "+brand_id+" and franchise_id = "+franchise_id+" and room_number = "+room_number+" and amenity = '"+deleteAmenity+"'");
		stmt.close();
	}
	
	public void EmployeeRentingRoom(int cust_sin, int emp_sin, int brand_id, int franchise_id, int room_number, String booking_number, String arrival_date, String departure_date){
		try {
			Statement stmt = null;
			stmt = this.c.createStatement();
			//IMPLEMENT
			if(!isAvailable(brand_id, franchise_id, room_number, arrival_date, departure_date)) {
				System.out.println("Date not available");
				return;
			}
			if(booking_number.equals("")){
				booking_number = "null";
			}
			stmt.executeUpdate( "INSERT INTO public.renting(renting_number, customer_sin, employee_sin, brand_id, franchise_id, room_number, booking_number, arrival_date, departure_date, archived) VALUES (DEFAULT, "+
			cust_sin+", "+emp_sin+", "+brand_id+", "+franchise_id+", "+room_number+", "+booking_number+", DEFAULT, '"+departure_date+"', false)  " );
		    	
		    
		      stmt.close();
		      System.out.println("***********");
		      System.out.println("Check In Succesful!");
		      System.out.println("***********");
		      
		}  catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
	    }
	}
	
	public boolean isAvailable(int brand_id, int child_id, int room_number, String arrival_date, String departure_date) {
		
		boolean result = false;
		try {
			
			Statement stmt = null;
			stmt = this.c.createStatement();
			ResultSet rs2 = stmt.executeQuery( "SELECT checkdate("+brand_id+","+child_id+","+room_number+",'"+arrival_date+"'::date,'"+departure_date+"'::date);" );
			rs2.next();
			result = rs2.getBoolean("checkdate");
		}catch(Exception e) {
			e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
		}
		return result;
	}
	
	private String brandidToName(int brandid) {
		if (brandid == 1) {
			return "Best Western";
		} else if (brandid == 2) {
			return "Holiday Inn";
		} else if (brandid == 3) {
			return "SuperMagic Hotels";
		} else if (brandid == 4) {
			return "Best Eastern";
		} else if (brandid == 5) {
			return "Les Suites";
		} else if (brandid == 6) {
			return "Delta";
		} else {
			return "NotSupported";
		}
	}
	
	public int returnNumHotels(String brand_name) throws Throwable {
		int num = 0;
		
			Statement stmt = null;
			stmt = this.c.createStatement();
			ResultSet rs2 = stmt.executeQuery( "SELECT number_of_hotels FROM parent_hotel_brand WHERE brand_name = '"+brand_name+"';" );
			boolean flag = rs2.next();
			if (flag) {
				num = rs2.getInt("number_of_hotels");
			} else {
				throw new Throwable("Result Set Empty");
			}
			
		return num;
	}
	
	public void queryFranchise(String brand_name, int franchise_id) {
		
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT a.street_number, a.street_name, a.city, a.province, a.country, a.number_of_rooms FROM hotel_franchise a,parent_hotel_brand b\r\n"
		    		+ "WHERE a.brand_id = b.brand_id AND b.brand_name = '"+brand_name+"' AND a.franchise_id = "+franchise_id+"; " );
		    
		    rs.next();
		    	
		    int street_num = rs.getInt("street_number");
		    String street_name = rs.getString("street_name");
		    String city = rs.getString("city");
		    String province = rs.getString("province");
		    String country = rs.getString("country");
		    int numRooms = rs.getInt("number_of_rooms");
		    
			System.out.println(brand_name + "'s Franchise " + franchise_id + " is located at " + street_num + " " + street_name + " in " +city+", "+province+", " +country+".");
			System.out.println("There are " + numRooms + " rooms at this location.");
			System.out.println("This location's email addresses are: ");
		    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
	
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT email_address FROM inst_franchise_email, parent_hotel_brand\r\n"
		    		+ "WHERE inst_franchise_email.brand_id = parent_hotel_brand.brand_id AND parent_hotel_brand.brand_name = '"+brand_name+"' AND\r\n"
		    		+ "inst_franchise_email.franchise_id = "+franchise_id+"; " );
		    
		    while ( rs.next() ) {
		    	
		    	 String email = rs.getString("email_address");
		    	 System.out.println(email);
		    }    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
		
		System.out.println("This location's phone numbers are: ");
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT phone_number FROM inst_franchise_phone,parent_hotel_brand\r\n"
		    		+ "WHERE inst_franchise_phone.brand_id = parent_hotel_brand.brand_id AND parent_hotel_brand.brand_name = '"+brand_name+"' AND\r\n"
		    		+ "inst_franchise_phone.franchise_id = "+franchise_id+"; " );
		    
		    while ( rs.next() ) {
		    	 String phonenum = rs.getString("phone_number");
		    	 System.out.println(phonenum);
		    }    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
		
	}

	public void GetCustomerData(int sin) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery("select * from customer where sin_number = '"+sin+"'");
		System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", "CUSTOMER SIN", "FIRST NAME", "MIDDLE INITIAL", "LAST NAME", "STREET NUMBER", "STREET NAME", "UNIT NUMBER", "CITY", "PROVINCE", "COUNTRY", "POSTAL CODE", "REGISTRATION DATE");
		System.out.println();
		rs.next();
		String first_name = rs.getString("first_name");
		String middle_initial = rs.getString("middle_initial");
		String last_name = rs.getString("last_name");
		int street_number = rs.getInt("street_number");
		String street_name = rs.getString("street_name");
		int unit_number = rs.getInt("unit_number");
		String city = rs.getString("city");
		String province = rs.getString("province");
		String country = rs.getString("country");
		String postal_code = rs.getString("postal_code");
		String registration_date = rs.getString("registration_date");
		System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", sin, first_name, middle_initial, last_name, street_number, street_name, unit_number, city, province, country, postal_code, registration_date);
		System.out.println();
		stmt.close();
		   
	}

	public void EditCustomerAddress(int c_sin, int street_number, String street_name, int unit_number, String city,
			String province, String country, String postal_code) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		stmt.executeUpdate("update customer set street_number = "+street_number+", street_name = '"+street_name+"', unit_number = "+unit_number+", city = '"+city+"', province = '"+province+"', country = '"+country+"', postal_code = '"+postal_code+"' where sin_number = '"+c_sin+"'");
		System.out.println("***********");
	    System.out.println("Address Change Succesful!");
	    System.out.println("***********");
		stmt.close();
	}

	public void GetReservationsByCustomer(int sin) throws SQLException {
		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery("select * from booking where customer_sin = '"+sin+"'");
		
		System.out.println("Here are the bookings for customer "+sin+":");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s", "BOOKING NUMBER", "ROOM_NUMBER", "RENTING NUMBER", "ARRIVAL DATE", "DEPARTURE DATE", "ARCHIVED?");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        
		while(rs.next()) {
			int booking_number = rs.getInt("booking_number");
			int room_number = rs.getInt("room_number");
			int renting_number = rs.getInt("renting_number");
			String arrival_date = rs.getString("arrival_date");
			String departure_date = rs.getString("departure_date");
			boolean archived = rs.getBoolean("archived");
			System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s", booking_number, room_number, renting_number, arrival_date, departure_date, archived);
	        System.out.println();
			}
		rs.close();
		System.out.println();
		ResultSet rs2 = stmt.executeQuery("select * from renting where customer_sin = '"+sin+"'");
		System.out.println("Here are the rentings for customer "+sin+":");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s", "RENTING NUMBER", "CUSTOMER SIN", "RENTING NUMBER", "ARRIVAL DATE", "DEPARTURE DATE", "ARCHIVED?", "PAYMENT");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		while(rs2.next()) {
			int renting_number = rs2.getInt("renting_number");
			int customer_sin = rs2.getInt("customer_sin");
			int booking_number = rs2.getInt("booking_number");
			String arrival_date = rs2.getString("arrival_date");
			String departure_date = rs2.getString("departure_date");
			boolean archived = rs2.getBoolean("archived");
			boolean payment = rs2.getBoolean("payment");
			System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s", renting_number, customer_sin, booking_number, arrival_date, departure_date, archived, payment);
	        System.out.println();
		
	}
}
}