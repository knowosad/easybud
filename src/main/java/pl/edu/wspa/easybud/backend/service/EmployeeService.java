package pl.edu.wspa.easybud.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wspa.easybud.backend.State;
import pl.edu.wspa.easybud.backend.entity.EmployeeEntity;
import pl.edu.wspa.easybud.backend.entity.OrderEntity;
import pl.edu.wspa.easybud.backend.repository.EmployeeRepository;
import pl.edu.wspa.easybud.backend.repository.OrderRepository;

import java.util.List;

@Service
public class EmployeeService {


    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void create(EmployeeEntity entity){
        employeeRepository.save(entity);
    }

    public List<EmployeeEntity> getEmployees () {
        return employeeRepository.findByStateEquals(State.ACTIVE.getName());
    }

    @Transactional
    public void update(EmployeeEntity employeeEntity) {
        EmployeeEntity entity = employeeRepository.findByNumber(employeeEntity.getNumber());
        entity.setLabel(employeeEntity.getLabel());
        entity.setFirstname(employeeEntity.getFirstname());
        entity.setLastname(employeeEntity.getLastname());
    }

    @Transactional
    public void delete(String number) {
        EmployeeEntity entity = employeeRepository.findByNumber(number);
        entity.setState(State.DELETED.getName());
    }

}


//    employees.add(new EmployeeEntity(id++, "Rowena", "Leeming", "rleeming0@bbc.co.uk", "Food Chemist"));
//    employees.add(new EmployeeEntity(id++, "Alvinia", "Delong", "adelong1@altervista.org", "Recruiting Manager"));
//    employees.add(new EmployeeEntity(id++, "Leodora", "Burry", "lburry2@example.com", "Food Chemist"));
//    employees.add(new EmployeeEntity(id++, "Karen", "Oaten", "koaten3@ihg.com", "VP Sales"));
//    employees.add(new EmployeeEntity(id++, "Mariele", "Huke", "mhuke4@washingtonpost.com", "Research Assistant IV"));
//    employees.add(new EmployeeEntity(id++, "Grata", "Widdowes", "gwiddowes5@cargocollective.com", "Actuary"));
//    employees.add(new EmployeeEntity(id++, "Donna", "Roadknight", "droadknight6@apache.org", "Mechanical Systems Engineer"));
//    employees.add(new EmployeeEntity(id++, "Tommi", "Nowland", "tnowland7@biblegateway.com", "Senior Developer"));
//    employees.add(new EmployeeEntity(id++, "Tonya", "Teresia", "tteresia8@boston.com", "Assistant Manager"));
//    employees.add(new EmployeeEntity(id++, "Steffen", "Yon", "syon9@ocn.ne.jp", "Senior Sales Associate"));
//    employees.add(new EmployeeEntity(id++, "Consalve", "Willes", "cwillesa@linkedin.com", "Programmer I"));
//    employees.add(new EmployeeEntity(id++, "Jeanelle", "Lambertz", "jlambertzb@nymag.com", "Operator"));
//    employees.add(new EmployeeEntity(id++, "Odelia", "Loker", "olokerc@gov.uk", "Developer I"));
//    employees.add(new EmployeeEntity(id++, "Briano", "Shawell", "bshawelld@posterous.com", "Research Assistant IV"));
//    employees.add(new EmployeeEntity(id++, "Tarrance", "Mainston", "tmainstone@cmu.edu", "Research Nurse"));
//    employees.add(new EmployeeEntity(id++, "Torrence", "Gehring", "tgehringf@a8.net", "Geological Engineer"));
//    employees.add(new EmployeeEntity(id++, "Augie", "Pionter", "apionterg@ehow.com", "Senior Financial Analyst"));
//    employees.add(new EmployeeEntity(id++, "Marillin", "Aveson", "mavesonh@shop-pro.jp", "Technical Writer"));
//    employees.add(new EmployeeEntity(id++, "Jacquelyn", "Moreby", "jmorebyi@slashdot.org", "Executive Secretary"));
//    employees.add(new EmployeeEntity(id++, "Glenn", "Bangley", "gbangleyj@prlog.org", "Account Executive"));
//    employees.add(new EmployeeEntity(id++, "Isidoro", "Glave", "iglavek@tamu.edu", "Compensation Analyst"));
//    employees.add(new EmployeeEntity(id++, "Cchaddie", "Spatarul", "cspatarull@sun.com", "Business Systems Development Analyst"));


