package com.example.service;

import com.example.model.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrScannerService {

    public static class OcrScanResult {
        public boolean success;
        public String extractedName;
        public String extractedCollege;
        public String extractedIdNumber;
        public String extractedEmail;
        public String extractedDepartment;
        public String rawText;

        public OcrScanResult(boolean success, String extractedName, String extractedCollege, 
                             String extractedIdNumber, String extractedEmail, String extractedDepartment, 
                             String rawText) {
            this.success = success;
            this.extractedName = extractedName;
            this.extractedCollege = extractedCollege;
            this.extractedIdNumber = extractedIdNumber;
            this.extractedEmail = extractedEmail;
            this.extractedDepartment = extractedDepartment;
            this.rawText = rawText;
        }
    }

    public static OcrScanResult processCardScanSample(int sampleIndex) {
        switch (sampleIndex % 3) {
            case 0:
                return new OcrScanResult(
                    true,
                    "Ananthu Nair",
                    "College of Engineering Trivandrum (CET)",
                    "CET-2024-4091",
                    "ananthu.nair@cet.ac.in",
                    "CSE",
                    "COLLEGE OF ENGINEERING TRIVANDRUM (CET)\nSTUDENT IDENTIFICATION CARD\nNAME: ANANTHU NAIR\nID: CET-2024-4091\nDEPT: CSE\nVALIDATED CAMPUS CREDENTIAL"
                );
            case 1:
                return new OcrScanResult(
                    true,
                    "Parvathy Menon",
                    "Model Engineering College (MEC) Kochi",
                    "MEC-2023-1084",
                    "parvathy.menon@mec.ac.in",
                    "ECE",
                    "GOVT MODEL ENGINEERING COLLEGE KOCHI\nSTUDENT ID: MEC-2023-1084\nPARVATHY MENON\nDEPT: ECE\nSTATUS: ACTIVE GRADUATE"
                );
            default:
                return new OcrScanResult(
                    true,
                    "Rahul Pillai",
                    "Govt. Engineering College Thrissur (GECT)",
                    "GECT-2025-3312",
                    "rahul.pillai@gect.ac.in",
                    "ME",
                    "GOVT ENGINEERING COLLEGE THRISSUR\nCAMPUS ID #GECT-2025-3312\nRAHUL PILLAI\nDEPT: ME\nOFFICIAL EMAIL VERIFIED"
                );
        }
    }

    public static User convertScanToUser(OcrScanResult scanResult) {
        if (!scanResult.success) {
            return new User();
        }
        return new User(
            "usr_" + System.currentTimeMillis() % 10000,
            scanResult.extractedName,
            scanResult.extractedCollege,
            scanResult.extractedIdNumber,
            scanResult.extractedEmail,
            scanResult.extractedDepartment,
            5.0,
            1,
            true
        );
    }
}
