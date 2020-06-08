

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


// get custom fields
def customFields = get("/rest/api/2/field")
        .asObject(List)
        .body
        .findAll { (it as Map).custom } as List<Map>

def input1CfId = customFields.find { it.name == 'Providencia de autos firme' }?.id
def input2CfId = customFields.find { it.name == 'Tipo de resolución' }?.id
def outputCfId = customFields.find { it.name == 'Fecha de vencimiento del plazo' }?.id
def projectKey = "SAL1"

if (issue == null || ((Map)issue.fields.project).key != projectKey) {
    logger.info("Wrong Project ${issue.fields.project.key}")
    return
}

def input1 = issue.fields[input1CfId] as String
def input2 = issue.fields[input2CfId].value as String

if (input1 == null || input2 == null) {
    logger.info("Calculation using ${input1} and ${input2} was not possible")
    return
}

logger.info("values for input1: ${input1} and input2: ${input2} ")
def output = "";

if (input2 == 'AI') {
    output = AddBusinessDays(input1, 15); //input1 // + input2
    logger.info(input1 + " +15");
}else if(input2 == 'AS') { 
   output = AddBusinessDays(input1, 60); //input1 // + input2
    logger.info(input1 + " +60");
}


if (output == (issue.fields[outputCfId] as String)) {
    logger.info("already been updated")
    return
}

put("/rest/api/2/issue/${issue.key}")
        .header("Content-Type", "application/json")
        .body([
        fields:[
                (outputCfId): output
        ]
])
        .asString()
