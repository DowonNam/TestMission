package com.example.ms1;

import com.example.ms1.Notebook.Notebook;
import com.example.ms1.Notebook.NotebookRepository;
import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteRepository;
import com.example.ms1.note.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NotebookRepository notebookRepository;
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @RequestMapping("/")
    public String main(Model model) {
        //1. DB에서 데이터 꺼내오기
        List<Notebook> notebookList = notebookRepository.findAll();
        if(notebookList.isEmpty()){
            Notebook notebook = new Notebook();
            notebook.setName("새로운 노트북");
            notebookRepository.save(notebook);

            return "redirect:/";
        }
        Notebook targetNotebook = notebookList.get(0);
        List<Note> noteList = noteRepository.findByNotebook(targetNotebook);
        if(noteList.isEmpty()){
            Note note = noteService.saveDefault();
            targetNotebook.addNote(note);
            notebookRepository.save(targetNotebook);

            return "redirect:/";
        }

        model.addAttribute("notebookList",notebookList);
        model.addAttribute("targetNotebook",targetNotebook);
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));

        return "main";
    }

}
