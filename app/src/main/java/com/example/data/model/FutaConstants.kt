package com.example.data.model

object FutaConstants {
    val LOCATIONS = listOf(
        "South Gate",
        "North Gate",
        "Obakekere",
        "Obanla",
        "FUTA Junction",
        "Aule Road",
        "Roadblock",
        "Champion Junction",
        "Stateline",
        "West Gate"
    )

    val FACULTIES_AND_DEPARTMENTS = mapOf(
        "School of Engineering & Engineering Tech (SEET)" to listOf(
            "Industrial & Production Engineering",
            "Mechanical Engineering",
            "Electrical & Electronics Engineering",
            "Civil & Environmental Engineering",
            "Mining Engineering",
            "Agricultural & Environmental Engineering",
            "Metallurgical & Materials Engineering"
        ),
        "School of Computing (SOC)" to listOf(
            "Computer Science",
            "Software Engineering",
            "Cyber Security",
            "Information Technology",
            "Information Systems"
        ),
        "School of Environmental Technology (SET)" to listOf(
            "Architecture",
            "Estate Management",
            "Quantity Surveying",
            "Urban & Regional Planning",
            "Building Technology"
        ),
        "School of Sciences (SOS)" to listOf(
            "Biochemistry",
            "Microbiology",
            "Physics / Electronics",
            "Chemistry",
            "Mathematical Sciences"
        ),
        "School of Agriculture & Agricultural Tech (SAAT)" to listOf(
            "Animal Production & Health",
            "Crop, Soil & Pest Management",
            "Ecotourism & Wildlife Management",
            "Fisheries & Aquaculture Technology",
            "Forestry & Wood Technology"
        ),
        "School of Management Technology (SMAT)" to listOf(
            "Project Management Technology",
            "Transport Management Technology",
            "Entrepreneurship"
        )
    )

    val AMENITIES_LIST = listOf(
        "Borehole Water",
        "Prepaid Meter",
        "Fenced & Security Guard",
        "Generator / Inverter Allowed",
        "Reading Desk Space",
        "Private Balcony",
        "Modern Tiled Floor",
        "En-suite Bathroom",
        "Kitchen Wardrobe",
        "Quiet Environment"
    )

    fun getInitialStudents(): List<Student> = listOf(
        Student(
            id = "student_tunde",
            fullName = "Tunde Adeleke",
            email = "tadeleke@futa.edu.ng",
            phone = "0803 234 5678",
            matricNumber = "IPE/22/4512",
            department = "Industrial & Production Engineering",
            faculty = "School of Engineering & Engineering Tech (SEET)",
            level = AcademicLevel.LEVEL_200,
            religion = "Christianity",
            gender = "Male",
            bio = "Quiet, organized 200L engineering student. I spend most weekdays in the SEET workshop and library. Looking for someone responsible who respects study time.",
            avatarInitials = "TA",
            avatarColorHex = 0xFF0A2540,
            verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
            showPhonePublicly = false,
            showReligionPublicly = true,
            habits = listOf(
                LifestyleHabit.STUDY_IN_SILENCE,
                LifestyleHabit.VERY_CLEAN_DAILY,
                LifestyleHabit.NON_SMOKER,
                LifestyleHabit.GUESTS_ALLOWED_DAY_ONLY
            ),
            accommodationStatus = AccommodationStatus.LOOKING_FOR_APARTMENT,
            preferredLocation = "South Gate",
            budgetNaira = 130_000
        ),
        Student(
            id = "student_chioma",
            fullName = "Chioma Nwosu",
            email = "cnwosu@futa.edu.ng",
            phone = "0814 987 6543",
            matricNumber = "CSC/21/8721",
            department = "Computer Science",
            faculty = "School of Computing (SOC)",
            level = AcademicLevel.LEVEL_300,
            religion = "Christianity",
            gender = "Female",
            bio = "300L SOC student & mobile developer. I have a clean self-contain at South Gate with constant borehole water and pre-paid meter. Need a peaceful roommate to split rent.",
            avatarInitials = "CN",
            avatarColorHex = 0xFF059669,
            verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
            showPhonePublicly = false,
            showReligionPublicly = false,
            habits = listOf(
                LifestyleHabit.NIGHT_OWL,
                LifestyleHabit.MODERATE_CLEANING,
                LifestyleHabit.NON_SMOKER,
                LifestyleHabit.COOKS_REGULARLY
            ),
            accommodationStatus = AccommodationStatus.HAS_APARTMENT,
            preferredLocation = "South Gate",
            budgetNaira = 140_000,
            hasApartmentDetails = ApartmentDetails(
                location = "South Gate (Behind God's Grace Villa)",
                totalRentNaira = 280_000,
                individualRentNaira = 140_000,
                availableSpaces = 1,
                totalRooms = 1,
                apartmentType = "Self-Contain with Balcony",
                facilities = listOf("Borehole Water", "Prepaid Meter", "Fenced & Security Guard", "Modern Tiled Floor"),
                description = "Well-ventilated self-contain flat with tiled bathroom, private balcony, and prepaid meter.",
                moveInDate = "Next Semester / Immediate"
            )
        ),
        Student(
            id = "student_segun",
            fullName = "Segun Bakare",
            email = "sbakare@futa.edu.ng",
            phone = "0708 112 3344",
            matricNumber = "ARC/20/3310",
            department = "Architecture",
            faculty = "School of Environmental Technology (SET)",
            level = AcademicLevel.LEVEL_400,
            religion = "Islam",
            gender = "Male",
            bio = "400L Architecture senior. Mostly drafting in studio or working late nights on 3D renderings. Looking for a partner to co-lease a 2-room flat around Obakekere or North Gate.",
            avatarInitials = "SB",
            avatarColorHex = 0xFF7C3AED,
            verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
            showPhonePublicly = false,
            showReligionPublicly = true,
            habits = listOf(
                LifestyleHabit.NIGHT_OWL,
                LifestyleHabit.BACKGROUND_MUSIC,
                LifestyleHabit.NON_SMOKER,
                LifestyleHabit.RARE_GUESTS
            ),
            accommodationStatus = AccommodationStatus.LOOKING_FOR_PARTNER,
            preferredLocation = "North Gate",
            budgetNaira = 160_000
        ),
        Student(
            id = "student_aminat",
            fullName = "Aminat Bello",
            email = "abello@futa.edu.ng",
            phone = "0809 555 4321",
            matricNumber = "BCH/23/1094",
            department = "Biochemistry",
            faculty = "School of Sciences (SOS)",
            level = AcademicLevel.LEVEL_200,
            religion = "Islam",
            gender = "Female",
            bio = "200L Biochemistry student. Friendly, neat, and serious with academics. Looking for a female roommate around Aule or FUTA Junction who values quiet study evenings.",
            avatarInitials = "AB",
            avatarColorHex = 0xFFD97706,
            verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
            showPhonePublicly = false,
            showReligionPublicly = true,
            habits = listOf(
                LifestyleHabit.EARLY_BIRD,
                LifestyleHabit.STUDY_IN_SILENCE,
                LifestyleHabit.VERY_CLEAN_DAILY,
                LifestyleHabit.COOKS_REGULARLY
            ),
            accommodationStatus = AccommodationStatus.LOOKING_FOR_APARTMENT,
            preferredLocation = "Aule Road",
            budgetNaira = 110_000
        ),
        Student(
            id = "student_kazeem",
            fullName = "Kazeem Oladipo",
            email = "koladipo@futa.edu.ng",
            phone = "0816 777 8899",
            matricNumber = "MEE/22/6701",
            department = "Mechanical Engineering",
            faculty = "School of Engineering & Engineering Tech (SEET)",
            level = AcademicLevel.LEVEL_200,
            religion = "Christianity",
            gender = "Male",
            bio = "200L Mech Eng. Has a spacious 2-bedroom flat at Roadblock with constant light and personal generator slot. Looking for 1 roommate to occupy the second bedroom.",
            avatarInitials = "KO",
            avatarColorHex = 0xFF0284C7,
            verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
            showPhonePublicly = false,
            showReligionPublicly = false,
            habits = listOf(
                LifestyleHabit.VERY_CLEAN_DAILY,
                LifestyleHabit.NON_SMOKER,
                LifestyleHabit.GUESTS_ALLOWED_DAY_ONLY
            ),
            accommodationStatus = AccommodationStatus.HAS_APARTMENT,
            preferredLocation = "Roadblock",
            budgetNaira = 150_000,
            hasApartmentDetails = ApartmentDetails(
                location = "Roadblock, Opposite NNPC Mega Station",
                totalRentNaira = 300_000,
                individualRentNaira = 150_000,
                availableSpaces = 1,
                totalRooms = 2,
                apartmentType = "2-Bedroom Flat",
                facilities = listOf("Generator / Inverter Allowed", "Borehole Water", "Prepaid Meter", "Fenced & Security Guard"),
                description = "Standard 2-bedroom flat. Big parlor, separate kitchen, and clean water supply. FUTA campus shuttle stops right outside.",
                moveInDate = "Immediate"
            )
        )
    )

    fun getInitialListings(): List<ApartmentListing> = listOf(
        ApartmentListing(
            id = "listing_1",
            ownerId = "student_chioma",
            ownerName = "Chioma Nwosu",
            ownerDepartment = "Computer Science",
            ownerLevel = "300L",
            title = "Modern Self-Contain at South Gate (1 Roommate needed)",
            location = "South Gate",
            totalRentNaira = 280_000,
            individualRentNaira = 140_000,
            availableSpaces = 1,
            totalRooms = 1,
            apartmentType = "Self-Contain",
            facilities = listOf("Borehole Water", "Prepaid Meter", "Fenced & Security Guard", "Tiled Floor"),
            description = "Quiet and secure lodge just a 4-minute walk to FUTA South Gate. Shared kitchen and balcony. Looking for an organized female student.",
            moveInDate = "Next Semester / Immediate",
            isOccupied = false,
            isVerifiedApartment = true
        ),
        ApartmentListing(
            id = "listing_2",
            ownerId = "student_kazeem",
            ownerName = "Kazeem Oladipo",
            ownerDepartment = "Mechanical Engineering",
            ownerLevel = "200L",
            title = "Spacious 2-Bedroom Flat (Private Room Available)",
            location = "Roadblock",
            totalRentNaira = 300_000,
            individualRentNaira = 150_000,
            availableSpaces = 1,
            totalRooms = 2,
            apartmentType = "2-Bedroom Flat",
            facilities = listOf("Prepaid Meter", "Borehole Water", "Generator Allowed", "Security Guard", "Balcony"),
            description = "You get your own private bedroom and bathroom in a 2-bed flat. Campus bus available right in front of the gate. Electricity is very stable.",
            moveInDate = "Immediate",
            isOccupied = false,
            isVerifiedApartment = true
        ),
        ApartmentListing(
            id = "listing_3",
            ownerId = "student_daniel",
            ownerName = "Daniel Ogunleye",
            ownerDepartment = "Quantity Surveying",
            ownerLevel = "400L",
            title = "En-suite Room in 3-Bedroom Lodge near North Gate",
            location = "North Gate",
            totalRentNaira = 360_000,
            individualRentNaira = 120_000,
            availableSpaces = 1,
            totalRooms = 3,
            apartmentType = "3-Bedroom Lodge",
            facilities = listOf("Borehole Water", "En-suite Bathroom", "Fenced & Gated", "Prepaid Meter"),
            description = "Very close to North Gate entrance. Clean water, serene study environment, each room has separate light sub-meter.",
            moveInDate = "Start of Next Session",
            isOccupied = false,
            isVerifiedApartment = true
        )
    )
}
