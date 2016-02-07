package pl.pogorzelski.webconverter.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.Role;
import pl.pogorzelski.webconverter.domain.dto.CurrentUser;
import pl.pogorzelski.webconverter.queue.ConvertTask;
import pl.pogorzelski.webconverter.queue.MyExecutorService;
import pl.pogorzelski.webconverter.service.UserService;
import pl.pogorzelski.webconverter.util.TaskDiscoverUtil;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kuba
 */
@RequestMapping("/tasks")
@Controller
public class TaskManageController {

    @Inject
    private MyExecutorService myExecutorService;

    @Inject
    private UserService userService;

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping
    public ModelAndView viewTasks(CurrentUser currentUser) {
        if (!(currentUser.getRole() == Role.ADMIN)) {
            return new ModelAndView("redirect:/tasks/user/" + currentUser.getId());
        }
        List<ConvertTask> waitingTasks = getConvertTaskStream().collect(Collectors.toList());
        List<ConvertTask> inProgressOrFinishedTasks = myExecutorService.getFinishedTasks();

        inProgressOrFinishedTasks = reverseList(inProgressOrFinishedTasks);

        ModelAndView modelAndView = getModelAndView(waitingTasks, inProgressOrFinishedTasks);
        return modelAndView;
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping("/user/{id}")
    public ModelAndView viewTasksForUser(@PathVariable Long id) {
        Predicate<ConvertTask> convertTaskPredicate = c -> {
            Long taskUserId = c.getUser().getId();
            return taskUserId.equals(userService.getUserById(id).get().getId());
        };
        List<ConvertTask> waitingTasks = getConvertTaskStream().filter(convertTaskPredicate)
                .collect(Collectors.toList());
        List<ConvertTask> inProgressOrFinishedTasks = myExecutorService.getFinishedTasks().stream().filter
                (convertTaskPredicate).collect(Collectors.toList());
        inProgressOrFinishedTasks = reverseList(inProgressOrFinishedTasks);

        ModelAndView modelAndView = getModelAndView(waitingTasks, inProgressOrFinishedTasks);
        return modelAndView;
    }

    private Stream<ConvertTask> getConvertTaskStream() {
        return myExecutorService.getExecutorService().getQueue()
                .stream().map(r -> (ConvertTask) TaskDiscoverUtil.findRealTask(r));
    }

    private List reverseList(List<ConvertTask> inProgressOrFinishedTasks) {
        ConvertTask[] convertTasks = inProgressOrFinishedTasks.toArray(
                new ConvertTask[inProgressOrFinishedTasks.size()]);
        CollectionUtils.reverseArray(convertTasks);
        return Arrays.asList(convertTasks);
    }

    private ModelAndView getModelAndView(List<ConvertTask> waitingTasks, List<ConvertTask> inProgressOrFinishedTasks) {
        ModelAndView modelAndView = new ModelAndView("tasks");
        modelAndView.addObject("waitingTasks", waitingTasks);
        modelAndView.addObject("inProgressOrFinishedTasks", inProgressOrFinishedTasks);
        return modelAndView;
    }

}
