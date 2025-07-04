package uz.tuit.unirules.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateDisciplineRuleReqDto;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;
import uz.tuit.unirules.services.discipline_rule.DisciplineRuleService;

import java.util.List;

//restcontroller anotatisiyasi bu spring frameworkda @RespondBody va @Controller anotatsiyalarining kombinatsiyasi. bu Restful Api yaratishda xizmat qiladi. bu annotatsiya qoyilgan klassdagi metodlar faqat json yoki xml korinishida javob qaytaradi. bu yerda hech qanday html sahifa yoki view qaytarilmaydi, faqat malumot qaytariladi. agar frontenda react yoki angular ishlatilsa backenda restcontroller ishlatish kerak, @controller view, html bilan ishlaydi.
@RestController
// requestmapping annotantioni spring frameworkda https sorovlarni klasslarga yoki metodlarga boglash uchun ishlatiladi. bu annotatsiay orqali qaysi url qaysi klass yoki metodga tegishli ekanini belgilanadi. bu annotatsiya klasdagi barcha metodlar uchun asosiy yol bolib xizmat qiladi.
@RequestMapping("/api/discipline-rule")
@Tag(name = "DisciplineRule Management ")
public class DisciplineRuleController {
    private final DisciplineRuleService disciplineRuleService;

    public DisciplineRuleController(DisciplineRuleService disciplineRuleService) {
        this.disciplineRuleService = disciplineRuleService;
    }

    //@Validated bu validation beani uchun yani obyektlarni tekshirish uchun ishlatiladi.Spring frameworkdan kelgan
    //@RequestBody bu user malumotni json korinishida Post yoki Put methodlari orqali yuboradigan bolsa shundagina @RequestBody qoyiladi.

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping//yangi malumot qoshish uchun create uchun ishlatiladi.
    public ApiResponse<DisciplineRuleRespDto> create(@Validated @RequestBody CreateDisciplineRuleReqDto createDisciplineRuleReqDto) {
        return disciplineRuleService.create(createDisciplineRuleReqDto);
    }

    //@RequestParam bu agar user malumotni key value shaklida yuboradigan bolsa u holda bu anotatsiyadan foydalaniladi. agar 2 tadan koproq ozgaruvchi boldadigan bolsa restfull Api da requestParam tezroq va toza Pathvariablega nisbatan.
    //@pathvariable bu springda ishlatiladigan anotatsiya bolib url yolida berilgan ozgaruvchini(parametr) tutib olish uchun ishlatiladi.

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    @GetMapping("/{id}")// malumotni olish(read uchun) ishlatiladi
    public ApiResponse<DisciplineRuleRespDto> get(@PathVariable(value = "id") Long entityId) {
        return disciplineRuleService.get(entityId);
    }



    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")// malumotni yangilash uchun ishlatiladi updae uchun
    public ApiResponse<DisciplineRuleRespDto> update(@PathVariable(value = "id") Long entityId,
                                                     @Validated @RequestBody UpdateDisciplineRuleReqDto updateDisciplineRuleReqDto) {
        return disciplineRuleService.update(entityId, updateDisciplineRuleReqDto);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")// malumotni ochirish uchun delete uchun ishlatiladi
    public ApiResponse<DisciplineRuleRespDto> delete(@PathVariable(value = "id") Long entityId) {
        return disciplineRuleService.delete(entityId);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public ApiResponse<List<DisciplineRuleRespDto>> getAll() {
        return disciplineRuleService.getAll();
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ApiResponse<Page<DisciplineRuleRespDto>> findAllPagination(Pageable pageable) {
        return disciplineRuleService.findAllPages(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    @GetMapping("/for-student")
    public ApiResponse<List<DisciplineRuleRespDto>> getAllPaginationForStudent(Pageable pageable) {
        return disciplineRuleService.getAllPaginationForStudent(pageable);
    }
}
