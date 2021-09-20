package org.elsys.ip.maven.mainModule;

import com.google.common.collect.ImmutableSet;
import lombok.val;
import lombok.var;
import maven.ourModule.ClassInModule;

public class MainClass {
    public static void main(String[] args) {
        var set1 = ImmutableSet.of("1", "2", "3");
        val set2 = ImmutableSet.of("3", "2", "1");
        System.out.println(set1.equals(set2));

        Student s1 = new Student(true, "Ivan", "12V");
        System.out.println(s1);

        System.out.println(new ClassInModule().getString());
    }


}
