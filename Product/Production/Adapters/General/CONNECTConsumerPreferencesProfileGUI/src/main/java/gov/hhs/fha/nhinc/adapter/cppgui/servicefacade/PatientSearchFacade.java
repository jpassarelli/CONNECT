/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.cppgui.servicefacade;

import gov.hhs.fha.nhinc.adapter.cppgui.PatientSearchCriteria;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author patlollav
 */
public class PatientSearchFacade {


    public List<PatientVO> searchPatient(PatientSearchCriteria patientSearchCriteria) throws Exception
    {

        List<PatientVO> patientVOs = null;

        PRPAIN201305UV02 searchRequest = createPRPAMT201301UVPatient(patientSearchCriteria);

        AdapterMpiProxy mpiProxy = getAdapterMpiProxy();

        PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchRequest);

        Patients mpiPatients = convertPRPAIN201306UVToPatients(patients);

        patientVOs = convertMPIPatientToPatientVO(mpiPatients);

        return patientVOs;
    }

    /**
     * 
     * @param patientSearchCriteria
     * @return
     */
    private PRPAIN201305UV02 createPRPAMT201301UVPatient(PatientSearchCriteria patientSearchCriteria) {

        II patId = new II();
        patId.setExtension(patientSearchCriteria.getPatientID());
        patId.setRoot(patientSearchCriteria.getAssigningAuthorityID());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(patientSearchCriteria.getFirstName(), patientSearchCriteria.getLastName(), null, null, null), patId);
        PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getAssigningAuthorityID());
        return searchPat;
    }

    /**
     * 
     * @return
     */
    private AdapterMpiProxy getAdapterMpiProxy() {
        AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
        AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
        return mpiProxy;
    }

    /**
     * 
     * @param patients
     * @return
     */
    private Patients convertPRPAIN201306UVToPatients(PRPAIN201306UV02 patients) {
        Patients mpiPatients = new Patients();
        Patient searchPatient = new Patient();

        if (patients != null &&
                patients.getControlActProcess() != null &&
                NullChecker.isNotNullish(patients.getControlActProcess().getSubject()) &&
                patients.getControlActProcess().getSubject().get(0) != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
            PRPAMT201310UV02Patient mpiPatResult = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();

            if (NullChecker.isNotNullish(mpiPatResult.getId()) &&
                    mpiPatResult.getId().get(0) != null &&
                    NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(), mpiPatResult.getId().get(0).getRoot());
            }

            if (mpiPatResult.getPatientPerson() != null &&
                    mpiPatResult.getPatientPerson().getValue() != null &&
                    mpiPatResult.getPatientPerson().getValue().getName() != null) {
                PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue().getName());
                PersonName personName = new PersonName();
                personName.setFirstName(name.getGivenName());
                personName.setLastName(name.getFamilyName());
                searchPatient.setName(personName);
            }

            mpiPatients.add(searchPatient);
        }

        return mpiPatients;
    }

    /**
     *
     * @return
     */
    private List<PatientVO> convertMPIPatientToPatientVO(Patients mpiPatients) throws Exception
    {
        List<PatientVO> patientVOs = new ArrayList<PatientVO>();

        if (mpiPatients == null || mpiPatients.size() < 1) {
            return patientVOs;
        }

        String assigningAuthId = PropertyAccessor.getProperty("adapter", "assigningAuthorityId");


        for (Patient patient : mpiPatients)
        {
            PatientVO patientVO = new PatientVO();

            PersonName name = patient.getName();

            if (name != null) {
                patientVO.setFirstName(name.getFirstName());
                patientVO.setLastName(name.getLastName());
            }

            for (Identifier id : patient.getIdentifiers())
            {
                if (id.getOrganizationId().equals(assigningAuthId)) 
                {
                    patientVO.setAssigningAuthorityID(assigningAuthId);
                    patientVO.setPatientID(id.getId());
                    patientVO.setOrganizationID(id.getOrganizationId());
                }
            }

            patientVOs.add(patientVO);
        }

        return patientVOs;
    }
}