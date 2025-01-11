package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出登录")
    public Result<String> logout(HttpServletResponse response) {
        return Result.success();
    }

    /**
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping()
    @ApiOperation("新增员工")
    public Result addEmployee(EmployeeDTO employeeDTO) {
        log.info("新增员工 {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     *
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为{}", employeePageQueryDTO);
        PageResult result = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(result);
    }

    /**
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用和禁用员工账号")
    public Result changeStatus(@PathVariable("status") int status, long id){
        String s = status==0?"禁用":"启用";
        log.info("{}员工{}的账号", s, id);
        employeeService.setStatus(id, status);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("查询员工信息")
    public Result<Employee> getById(@PathVariable("id") long id){
        log.info("查询员工{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping()
    @ApiOperation("修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
