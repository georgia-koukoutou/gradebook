package com.koukoutou.gradebook.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koukoutou.gradebook.models.Course;
import com.koukoutou.gradebook.models.CourseGrade;
import com.koukoutou.gradebook.models.CourseGradeKey;
import com.koukoutou.gradebook.models.User;
import com.koukoutou.gradebook.repositories.CourseGradeRepository;
import com.koukoutou.gradebook.repositories.CourseRepository;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseGradeRepository courseGradeRepository;

    @GetMapping("/")
    public String index(Model model) {

        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);

        return "fragments/index";
    }

    @Transactional
    @PostMapping("/")
    public String addNewCourses(@AuthenticationPrincipal User user, HttpServletRequest request) {

        String[] courseIds = request.getParameterValues("course_id");
        String[] grades = request.getParameterValues("course_grade");

        if (courseIds != null && courseIds.length > 0 && grades != null && grades.length > 0) {

            for (int i = 0; i < courseIds.length; i++) {

                Course course = courseRepository.findById(Long.parseLong(courseIds[i])).get();
                double grade = Double.parseDouble(grades[i]);

                CourseGradeKey key = new CourseGradeKey(user.getId(), Long.parseLong(courseIds[i]));

                if (courseGradeRepository.existsById(key)) {
                    // update
                    courseGradeRepository.updateGrade(key.getCourseId(), key.getUserId(), grade);
                } else {
                    CourseGrade courseGrade = new CourseGrade();
                    courseGrade.setUser(user);
                    courseGrade.setCourse(course);
                    courseGrade.setGrade(grade);
                    courseGradeRepository.save(courseGrade);
                }
            }
        }

        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String showGrades(@AuthenticationPrincipal User user, Model model) {

        double sumOfWeights = 0;
        double sumOfGradesWeighted = 0;
        double finalGrade = 0;

        List<CourseGrade> userCourseGrades = courseGradeRepository.findByUserId(user.getId());
        model.addAttribute("courseGrades", userCourseGrades);

        for (CourseGrade courseGrade : userCourseGrades) {
            sumOfWeights += courseGrade.getCourse().getWeight();
            sumOfGradesWeighted += courseGrade.getGrade() * courseGrade.getCourse().getWeight();
        }

        finalGrade = sumOfGradesWeighted / sumOfWeights;
        model.addAttribute("grade", finalGrade);

        return "fragments/courses";
    }

    @GetMapping(value = { "/course", "/course/{id}" })
    public String showUpdateForm(@AuthenticationPrincipal User user, Model model, @PathVariable Long id) {

        CourseGrade courseGrade = courseGradeRepository.findByUserIdAndCourseId(user.getId(), id);
        model.addAttribute("courseGrade", courseGrade);

        return "fragments/edit_course_form";

    }

    @Transactional
    @PostMapping("/update")
    public String updateCourse(@AuthenticationPrincipal User user, @RequestParam("id") Long courseId,
            @RequestParam("grade") double grade) {

        courseGradeRepository.updateGrade(courseId, user.getId(), grade);
        return "redirect:/courses";
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteCourse(@AuthenticationPrincipal User user, @RequestParam("id") Long id) {

        courseGradeRepository.deleteByCourseIdAndUserId(id, user.getId());

        return "redirect:/courses";
    }
}