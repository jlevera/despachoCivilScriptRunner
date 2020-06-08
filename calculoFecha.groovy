

import static java.util.Calendar.*
import java.text.SimpleDateFormat;

NUMBEROFHOLIDAYS = 3;

// Field names
//Fecha de ingreso al gabinete  = customFieldid 10062
// Fecha de vencimiento del plazo = customFieldID 10046
// Tipo de resolución = customFieldID 10067
def boolean isItaHoliday(Calendar dateOfWeek)
{
	
if(dateOfWeek.get(Calendar.MONTH)==Calendar.MARCH && dateOfWeek.get(Calendar.DAY_OF_MONTH)==1)
{ 
		
		return true;
}



if(dateOfWeek.get(Calendar.MONTH)==Calendar.APRIL && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 9)
	{ 
		
		return true;
	}


if(dateOfWeek.get(Calendar.MONTH)==Calendar.MAY && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 1)
	{ 
		
		return true;
	}



if(dateOfWeek.get(Calendar.MONTH)==Calendar.MAY && (dateOfWeek.get(Calendar.DAY_OF_MONTH) == 14 || dateOfWeek.get(Calendar.DAY_OF_MONTH) == 15) )
	{ 
		
		return true;
	}



if(dateOfWeek.get(Calendar.MONTH)==Calendar.JUNE && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 12)
	{ 
		
		return true;
	}




if(dateOfWeek.get(Calendar.MONTH)==Calendar.AUGUST && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 15)
	{ 
		
		return true;
	}
	

if(dateOfWeek.get(Calendar.MONTH)==Calendar.SEPTEMBER && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 28)
	{ 
		
		return true;
	}



if(dateOfWeek.get(Calendar.MONTH)==Calendar.DECEMBER && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 8)
	{ 
		
		return true;
	}


if(dateOfWeek.get(Calendar.MONTH)==Calendar.DECEMBER && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 25)
	{ 
		
		return true;
	}



if(dateOfWeek.get(Calendar.MONTH)==Calendar.DECEMBER && dateOfWeek.get(Calendar.DAY_OF_MONTH) == 31)
	{ 
		
		return true;
	}

	// Feria Judicial de Enero y tambien 1 de Enero
	if(dateOfWeek.get(Calendar.MONTH)==Calendar.JANUARY)
	{ 
		//println "Enero";
		return true;
	}
	
	return false;
	
}

def String AddBusinessDays(String dateOfIssue, int daysToBeAdded)
{
	Calendar now = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	now.setTime(sdf.parse(dateOfIssue));// set date
 	 //def now = dateOfIssue; //Calendar.instance
	int days = daysToBeAdded;
	println now.time;

    for(int i=0;i<days;)
    {
        now.add(Calendar.DAY_OF_MONTH, 1);
        //here even sat and sun are added
        //but at the end it goes to the correct week day.
        //because i is only increased if it is week day

        //println now.get(Calendar.DAY_OF_WEEK) + " " + i + " " + now.time;
        if(!isItaHoliday(now) && now.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY && now.get(Calendar.DAY_OF_WEEK)!= Calendar.SATURDAY)
        {
            i++;
        }

    }
    // holidaysPY(now);
	return sdf.format(now.getTime());
	
	
}

// MAIN

print(AddBusinessDays("2020-03-01", 15));