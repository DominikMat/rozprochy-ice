
#ifndef DYNA_ICE
#define DYNA_ICE

module DynamicApp
{
    struct Employee {
        string name;
        string birthday;
        int age;
    };

    sequence<int> AgesSequence;

    interface BirthdayCheck {
        bool isTodayEmployeeBirthday(Employee emp);

        string getBirthdayWishes(string name);

        int predictNextBirthdayAge(AgesSequence previousAgesList);
    };
};

#endif
