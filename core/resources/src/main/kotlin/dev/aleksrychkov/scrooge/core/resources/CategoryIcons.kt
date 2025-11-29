package dev.aleksrychkov.scrooge.core.resources

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.LibraryBooks
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.automirrored.rounded.Wysiwyg
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.CarRepair
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.DinnerDining
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.material.icons.rounded.Domain
import androidx.compose.material.icons.rounded.DriveEta
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material.icons.rounded.Hiking
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Hotel
import androidx.compose.material.icons.rounded.HouseSiding
import androidx.compose.material.icons.rounded.Icecream
import androidx.compose.material.icons.rounded.LaptopMac
import androidx.compose.material.icons.rounded.LiveTv
import androidx.compose.material.icons.rounded.LocalAtm
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.LocalCarWash
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.LocalParking
import androidx.compose.material.icons.rounded.LocalPharmacy
import androidx.compose.material.icons.rounded.LocalTaxi
import androidx.compose.material.icons.rounded.Loyalty
import androidx.compose.material.icons.rounded.MoneyOff
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.OutdoorGrill
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PhoneIphone
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.SetMeal
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Sick
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Sports
import androidx.compose.material.icons.rounded.SportsBasketball
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.SportsHandball
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material.icons.rounded.TakeoutDining
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.ThumbsUpDown
import androidx.compose.material.icons.rounded.Vaccines
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.Work
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

val CategoryIcons: List<CategoryIcon> by lazy {
    listOf(
        // --------------------------------------------------------------------
        // INCOME ICONS (ID: I_01 to I_10)
        // --------------------------------------------------------------------
        CategoryIcon("I_01_ACCOUNT_BALANCE", Icons.Rounded.AccountBalance),
        CategoryIcon("I_02_WORK", Icons.Rounded.Work),
        CategoryIcon("I_03_TRENDING_UP", Icons.AutoMirrored.Rounded.TrendingUp),
        CategoryIcon("I_04_DOMAIN", Icons.Rounded.Domain),
        CategoryIcon("I_05_REDEEM", Icons.Rounded.Redeem),
        CategoryIcon("I_06_UNDO", Icons.AutoMirrored.Rounded.Undo),
        CategoryIcon("I_07_SAVINGS", Icons.Rounded.Savings),
        CategoryIcon("I_08_HANDYMAN", Icons.Rounded.Handyman),
        CategoryIcon("I_09_STARS", Icons.Rounded.Stars),
        CategoryIcon("I_10_STOREFRONT", Icons.Rounded.Storefront),

        // --------------------------------------------------------------------
        // HOUSING & UTILITIES ICONS (ID: H_01 to H_15)
        // --------------------------------------------------------------------
        CategoryIcon("H_01_HOME", Icons.Rounded.Home),
        CategoryIcon("H_02_GAVEL", Icons.Rounded.Gavel),
        CategoryIcon("H_03_HOUSE_SIDING", Icons.Rounded.HouseSiding),
        CategoryIcon("H_04_CONSTRUCTION", Icons.Rounded.Construction),
        CategoryIcon("H_05_CHAIR", Icons.Rounded.Chair),
        CategoryIcon("H_06_ELECTRIC_BOLT", Icons.Rounded.ElectricBolt),
        CategoryIcon("H_07_WATER_DROP", Icons.Rounded.WaterDrop),
        CategoryIcon("H_08_FIRE_DEPT", Icons.Rounded.LocalFireDepartment),
        CategoryIcon("H_09_WIFI", Icons.Rounded.Wifi),
        CategoryIcon("H_10_PHONE_IPHONE", Icons.Rounded.PhoneIphone),
        CategoryIcon("H_11_SUBSCRIPTIONS", Icons.Rounded.Subscriptions),
        CategoryIcon("H_12_DELETE", Icons.Rounded.Delete),
        CategoryIcon("H_13_SECURITY", Icons.Rounded.Security),
        CategoryIcon("H_14_FLORIST", Icons.Rounded.LocalFlorist),
        CategoryIcon("H_15_CLEANING", Icons.Rounded.CleaningServices),

        // --------------------------------------------------------------------
        // FOOD & DINING ICONS (ID: F_01 to F_15)
        // --------------------------------------------------------------------
        CategoryIcon("F_01_GROCERY_STORE", Icons.Rounded.LocalGroceryStore),
        CategoryIcon("F_02_RESTAURANT", Icons.Rounded.Restaurant),
        CategoryIcon("F_03_DINNER_DINING", Icons.Rounded.DinnerDining),
        CategoryIcon("F_04_TAKEOUT", Icons.Rounded.TakeoutDining),
        CategoryIcon("F_05_COFFEE", Icons.Rounded.Coffee),
        CategoryIcon("F_06_COOKIE", Icons.Rounded.Cookie),
        CategoryIcon("F_07_LOCAL_BAR", Icons.Rounded.LocalBar),
        CategoryIcon("F_08_BUSINESS_CENTER", Icons.Rounded.BusinessCenter),
        CategoryIcon("F_09_PETS", Icons.Rounded.Pets),
        CategoryIcon("F_10_CREATE", Icons.Rounded.Create),
        CategoryIcon("F_11_CAKE", Icons.Rounded.Cake),
        CategoryIcon("F_12_SHOPPING_CART", Icons.Rounded.ShoppingCart),
        CategoryIcon("F_13_SET_MEAL", Icons.Rounded.SetMeal),
        CategoryIcon("F_14_VACCINES", Icons.Rounded.Vaccines),
        CategoryIcon("F_15_ICECREAM", Icons.Rounded.Icecream),

        // --------------------------------------------------------------------
        // TRANSPORTATION ICONS (ID: T_01 to T_10)
        // --------------------------------------------------------------------
        CategoryIcon("T_01_GAS_STATION", Icons.Rounded.LocalGasStation),
        CategoryIcon("T_02_BUS", Icons.Rounded.DirectionsBus),
        CategoryIcon("T_03_TAXI", Icons.Rounded.LocalTaxi),
        CategoryIcon("T_04_PARKING", Icons.Rounded.LocalParking),
        CategoryIcon("T_05_DRIVE_ETA", Icons.Rounded.DriveEta),
        CategoryIcon("T_06_VERIFIED_USER", Icons.Rounded.VerifiedUser),
        CategoryIcon("T_07_CAR_REPAIR", Icons.Rounded.CarRepair),
        CategoryIcon("T_08_REPORT", Icons.Rounded.Report),
        CategoryIcon("T_09_DIRECTIONS_BIKE", Icons.AutoMirrored.Rounded.DirectionsBike),
        CategoryIcon("T_10_CAR_WASH", Icons.Rounded.LocalCarWash),

        // --------------------------------------------------------------------
        // HEALTH & PERSONAL ICONS (ID: P_01 to P_10)
        // --------------------------------------------------------------------
        CategoryIcon("P_01_HOSPITAL", Icons.Rounded.LocalHospital),
        CategoryIcon("P_02_SICK", Icons.Rounded.Sick),
        CategoryIcon("P_03_PHARMACY", Icons.Rounded.LocalPharmacy),
        CategoryIcon("P_04_MONITOR_HEART", Icons.Rounded.MonitorHeart),
        CategoryIcon("P_05_FITNESS_CENTER", Icons.Rounded.FitnessCenter),
        CategoryIcon("P_06_SELF_IMPROVEMENT", Icons.Rounded.SelfImprovement),
        CategoryIcon("P_07_LOYALTY", Icons.Rounded.Loyalty),
        CategoryIcon("P_08_SPORTS", Icons.Rounded.Sports),
        CategoryIcon("P_09_SPA", Icons.Rounded.Spa),
        CategoryIcon("P_10_VISIBILITY", Icons.Rounded.Visibility),

        // --------------------------------------------------------------------
        // ENTERTAINMENT & TRAVEL ICONS (ID: E_01 to E_10)
        // --------------------------------------------------------------------
        CategoryIcon("E_01_THEATERS", Icons.Rounded.Theaters),
        CategoryIcon("E_02_MUSIC_NOTE", Icons.Rounded.MusicNote),
        CategoryIcon("E_03_SPORTS_ESPORTS", Icons.Rounded.SportsEsports),
        CategoryIcon("E_04_LIVE_TV", Icons.Rounded.LiveTv),
        CategoryIcon("E_05_HEADSET", Icons.Rounded.Headset),
        CategoryIcon("E_06_FLIGHT", Icons.Rounded.Flight),
        CategoryIcon("E_07_HOTEL", Icons.Rounded.Hotel),
        CategoryIcon("E_08_PHOTO_CAMERA", Icons.Rounded.PhotoCamera),
        CategoryIcon("E_09_MUSEUM", Icons.Rounded.AccountBalance), // Reused
        CategoryIcon("E_10_HIKING", Icons.Rounded.Hiking),

        // --------------------------------------------------------------------
        // SHOPPING ICONS (ID: S_01 to S_10)
        // --------------------------------------------------------------------
        CategoryIcon("S_01_CHECKROOM", Icons.Rounded.Checkroom),
        CategoryIcon("S_02_STYLE", Icons.Rounded.Style),
        CategoryIcon("S_03_DIAMOND", Icons.Rounded.Diamond),
        CategoryIcon("S_04_DEVICES", Icons.Rounded.Devices),
        CategoryIcon("S_05_BOOK", Icons.Rounded.Book),
        CategoryIcon("S_06_GIFT_CARD", Icons.Rounded.CardGiftcard),
        CategoryIcon("S_07_SHOPPING_CART_2", Icons.Rounded.ShoppingCart), // Reused
        CategoryIcon("S_08_BRUSH", Icons.Rounded.Brush),
        CategoryIcon("S_09_SPORTS_HANDBALL", Icons.Rounded.SportsHandball),
        CategoryIcon("S_10_PLUMBING", Icons.Rounded.Plumbing),

        // --------------------------------------------------------------------
        // FINANCIAL & MISC ICONS (ID: M_01 to M_10)
        // --------------------------------------------------------------------
        CategoryIcon("M_01_CREDIT_CARD", Icons.Rounded.CreditCard),
        CategoryIcon("M_02_COMPARE_ARROWS", Icons.AutoMirrored.Rounded.CompareArrows),
        CategoryIcon("M_03_RECEIPT_LONG", Icons.AutoMirrored.Rounded.ReceiptLong),
        CategoryIcon("M_04_MONEY_OFF", Icons.Rounded.MoneyOff),
        CategoryIcon("M_05_LOCAL_ATM", Icons.Rounded.LocalAtm),
        CategoryIcon("M_06_SHOW_CHART", Icons.AutoMirrored.Rounded.ShowChart),
        CategoryIcon("M_07_PAID", Icons.Rounded.Paid),
        CategoryIcon("M_08_FAMILY_RESTROOM", Icons.Rounded.FamilyRestroom),
        CategoryIcon("M_09_BALANCE", Icons.Rounded.Balance),
        CategoryIcon("M_10_CALCULATE", Icons.Rounded.Calculate),

        // --------------------------------------------------------------------
        // EDUCATION & CHILDREN ICONS (ID: C_01 to C_10)
        // --------------------------------------------------------------------
        CategoryIcon("C_01_SCHOOL", Icons.Rounded.School),
        CategoryIcon("C_02_MENU_BOOK", Icons.AutoMirrored.Rounded.MenuBook),
        CategoryIcon("C_03_CHILD_CARE", Icons.Rounded.ChildCare),
        CategoryIcon("C_04_RECORD_VOICE_OVER", Icons.Rounded.RecordVoiceOver),
        CategoryIcon("C_05_SPORTS_BASKETBALL", Icons.Rounded.SportsBasketball),
        CategoryIcon("C_06_LIBRARY_BOOKS", Icons.AutoMirrored.Rounded.LibraryBooks),
        CategoryIcon("C_07_LAPTOP_MAC", Icons.Rounded.LaptopMac),
        CategoryIcon("C_08_ATTACH_MONEY", Icons.Rounded.AttachMoney),
        CategoryIcon("C_09_OUTDOOR_GRILL", Icons.Rounded.OutdoorGrill),
        CategoryIcon("C_10_APPS", Icons.Rounded.Apps),

        // --------------------------------------------------------------------
        // SOCIAL & COMMUNITY ICONS (ID: S_11 to S_19)
        // --------------------------------------------------------------------
        CategoryIcon("S_11_VOLUNTEER_ACTIVISM", Icons.Rounded.VolunteerActivism),
        CategoryIcon("S_12_GROUPS", Icons.Rounded.Groups),
        CategoryIcon("S_13_CELEBRATION", Icons.Rounded.Celebration),
        CategoryIcon("S_14_DIVERSITY_3", Icons.Rounded.Diversity3),
        CategoryIcon("S_15_WYSWYG", Icons.AutoMirrored.Rounded.Wysiwyg),
        CategoryIcon("S_16_NEWSPAPER", Icons.Rounded.Newspaper),
        CategoryIcon("S_17_CONFIRMATION_NUMBER", Icons.Rounded.ConfirmationNumber),
        CategoryIcon("S_18_THUMBS_UP_DOWN", Icons.Rounded.ThumbsUpDown),
        CategoryIcon("S_19_APARTMENT", Icons.Rounded.Apartment),
    )
}

@Immutable
data class CategoryIcon(val id: String, val icon: ImageVector)

val UncategorizedIcon: CategoryIcon by lazy {
    CategoryIcon("U_01_QUESTION_MARK", Icons.Rounded.QuestionMark)
}

val IconMapById: Map<String, CategoryIcon> by lazy { CategoryIcons.associateBy { it.id } }

val CategoryIconMap: Map<String, String> by lazy {
    mapOf(
        // --------------------------------------------------------------------
        // 1. INCOME (10 Categories)
        // --------------------------------------------------------------------
        "Salary" to "I_01_ACCOUNT_BALANCE",
        "Freelance/Gig Work" to "I_02_WORK",
        "Investment Dividends" to "I_03_TRENDING_UP",
        "Rental Income" to "I_04_DOMAIN",
        "Gifts Received" to "I_05_REDEEM",
        "Refunds" to "I_06_UNDO",
        "Interest Earned" to "I_07_SAVINGS",
        "Side Hustle" to "I_08_HANDYMAN",
        "Bonus" to "I_09_STARS",
        "Sale of Goods" to "I_10_STOREFRONT",

        // --------------------------------------------------------------------
        // 2. HOUSING & UTILITIES (15 Categories)
        // --------------------------------------------------------------------
        "Rent/Mortgage" to "H_01_HOME",
        "Property Tax" to "H_02_GAVEL",
        "Home Insurance" to "H_03_HOUSE_SIDING",
        "Repairs & Maintenance" to "H_04_CONSTRUCTION",
        "Decor & Furniture" to "H_05_CHAIR",
        "Electricity" to "H_06_ELECTRIC_BOLT",
        "Water & Sewage" to "H_07_WATER_DROP",
        "Natural Gas/Heating" to "H_08_FIRE_DEPT",
        "Internet Service" to "H_09_WIFI",
        "Mobile Phone" to "H_10_PHONE_IPHONE",
        "Cable/Streaming" to "H_11_SUBSCRIPTIONS",
        "Waste & Recycling" to "H_12_DELETE",
        "Security System" to "H_13_SECURITY",
        "Gardening/Lawn Care" to "H_14_FLORIST",
        "Cleaning Service" to "H_15_CLEANING",

        // --------------------------------------------------------------------
        // 3. FOOD & DINING (15 Categories)
        // --------------------------------------------------------------------
        "Groceries" to "F_01_GROCERY_STORE",
        "Cafes & Restaurants" to "F_02_RESTAURANT",
        "Fast Food" to "F_03_DINNER_DINING",
        "Takeout & Delivery" to "F_04_TAKEOUT",
        "Coffee & Tea" to "F_05_COFFEE",
        "Snacks & Candy" to "F_06_COOKIE",
        "Alcoholic Beverages" to "F_07_LOCAL_BAR",
        "Work Lunch" to "F_08_BUSINESS_CENTER",
        "Pet Food" to "F_09_PETS",
        "Office Supplies" to "F_10_CREATE",
        "Bakery" to "F_11_CAKE",
        "Farmer's Market" to "F_12_SHOPPING_CART",
        "Meal Prep Services" to "F_13_SET_MEAL",
        "Vitamins & Supplements" to "F_14_VACCINES",
        "Ice Cream/Dessert" to "F_15_ICECREAM",

        // --------------------------------------------------------------------
        // 4. TRANSPORTATION (10 Categories)
        // --------------------------------------------------------------------
        "Gas/Fuel" to "T_01_GAS_STATION",
        "Public Transport" to "T_02_BUS",
        "Taxi/Rideshare" to "T_03_TAXI",
        "Tolls & Parking" to "T_04_PARKING",
        "Car Payment" to "T_05_DRIVE_ETA",
        "Car Insurance" to "T_06_VERIFIED_USER",
        "Maintenance & Repairs" to "T_07_CAR_REPAIR",
        "Fines/Traffic Tickets" to "T_08_REPORT",
        "Bicycle Gear" to "T_09_DIRECTIONS_BIKE",
        "Car Wash" to "T_10_CAR_WASH",

        // --------------------------------------------------------------------
        // 5. HEALTH & PERSONAL (10 Categories)
        // --------------------------------------------------------------------
        "Doctor/Medical Fees" to "P_01_HOSPITAL",
        "Dentist" to "P_02_SICK",
        "Prescriptions/Pharmacy" to "P_03_PHARMACY",
        "Health Insurance" to "P_04_MONITOR_HEART",
        "Gym Membership" to "P_05_FITNESS_CENTER",
        "Hair/Beauty Care" to "P_06_SELF_IMPROVEMENT",
        "Cosmetics" to "P_07_LOYALTY",
        "Personal Trainer" to "P_08_SPORTS",
        "Spa/Massage" to "P_09_SPA",
        "Eyecare/Optometrist" to "P_10_VISIBILITY",

        // --------------------------------------------------------------------
        // 6. ENTERTAINMENT & TRAVEL (10 Categories)
        // --------------------------------------------------------------------
        "Movies & Theater" to "E_01_THEATERS",
        "Concerts & Shows" to "E_02_MUSIC_NOTE",
        "Video Games" to "E_03_SPORTS_ESPORTS",
        "Streaming Services (Video)" to "E_04_LIVE_TV",
        "Streaming Services (Music)" to "E_05_HEADSET",
        "Vacations/Flights" to "E_06_FLIGHT",
        "Accommodation/Hotels" to "E_07_HOTEL",
        "Souvenirs" to "E_08_PHOTO_CAMERA",
        "Museums & Attractions" to "E_09_MUSEUM",
        "Outdoor Activities" to "E_10_HIKING",

        // --------------------------------------------------------------------
        // 7. SHOPPING (10 Categories)
        // --------------------------------------------------------------------
        "Clothing" to "S_01_CHECKROOM",
        "Shoes" to "S_02_STYLE",
        "Accessories (Jewelry/Bags)" to "S_03_DIAMOND",
        "Electronics" to "S_04_DEVICES",
        "Books & Media" to "S_05_BOOK",
        "Gifts Given" to "S_06_GIFT_CARD",
        "Online Shopping (General)" to "S_07_SHOPPING_CART_2",
        "Hobbies & Crafts" to "S_08_BRUSH",
        "Toys & Games" to "S_09_SPORTS_HANDBALL",
        "Tools & Hardware" to "S_10_PLUMBING",

        // --------------------------------------------------------------------
        // 8. FINANCIAL & MISC (10 Categories)
        // --------------------------------------------------------------------
        "Credit Card Payment" to "M_01_CREDIT_CARD",
        "Loan Repayment" to "M_02_COMPARE_ARROWS",
        "Taxes" to "M_03_RECEIPT_LONG",
        "Bank Fees" to "M_04_MONEY_OFF",
        "ATM Withdrawals" to "M_05_LOCAL_ATM",
        "Investments/Brokerage" to "M_06_SHOW_CHART",
        "Savings Deposit" to "M_07_PAID",
        "Child Support/Alimony" to "M_08_FAMILY_RESTROOM",
        "Legal Fees" to "M_09_BALANCE",
        "Accounting Services" to "M_10_CALCULATE",

        // --------------------------------------------------------------------
        // 9. EDUCATION & CHILDREN (10 Categories)
        // --------------------------------------------------------------------
        "Tuition Fees" to "C_01_SCHOOL",
        "School Supplies" to "C_02_MENU_BOOK",
        "Childcare/Daycare" to "C_03_CHILD_CARE",
        "Tutoring" to "C_04_RECORD_VOICE_OVER",
        "Extracurricular" to "C_05_SPORTS_BASKETBALL",
        "Books (Educational)" to "C_06_LIBRARY_BOOKS",
        "Online Courses" to "C_07_LAPTOP_MAC",
        "Student Loan Payment" to "C_08_ATTACH_MONEY",
        "Summer Camp" to "C_09_OUTDOOR_GRILL",
        "Educational Software" to "C_10_APPS",

        // --------------------------------------------------------------------
        // 10. SOCIAL & COMMUNITY (10 Categories)
        // --------------------------------------------------------------------
        "Charity Donations" to "S_11_VOLUNTEER_ACTIVISM",
        "Friends & Family (Outings)" to "S_12_GROUPS",
        "Parties/Gatherings" to "S_13_CELEBRATION",
        "Club & Organization Fees" to "S_14_DIVERSITY_3",
        "Wedding/Event Gifts" to "S_15_WYSWYG",
        "Newspaper/Magazine" to "S_16_NEWSPAPER",
        "Social Events (Tickets)" to "S_17_CONFIRMATION_NUMBER",
        "Tipping" to "S_18_THUMBS_UP_DOWN",
        "Community Dues" to "S_19_APARTMENT",
    )
}
