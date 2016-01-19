package be.cegeka.viogate.patient;

import org.springframework.stereotype.Component;

@Component
class DummyPatientRepository implements PatientRepository {

    @Override
    public boolean isPatient(String vioNumber) {
        return true;
    }

}
