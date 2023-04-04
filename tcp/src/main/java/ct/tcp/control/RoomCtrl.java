package ct.tcp.control;

import ct.model.ChangedText;
import ct.model.File;
import ct.model.Project;
import ct.tcp.model.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomCtrl {
    private List<Room> rooms = new ArrayList<>();

    public void joinRoom(ServerProcess process, Project project) {
        for (Room room : rooms) {
            if (Objects.equals(room.roomId(), project.getId())) {
                room.joinRoom(process);
                return;
            }
        }
        rooms.add(new Room(project, process));
    }

    public String openFile(ServerProcess process, File file) throws IOException {
        for (Room room : rooms) {
            String data = room.openFile(process, file);
            if (data != null) {
                return data;
            }
        }
        throw new IOException();
    }

    public void changeText(ServerProcess process, ChangedText ct) {
        for (Room room : rooms) {
            room.changeText(process, ct);
        }
    }

    public String runProject(ServerProcess process, String input) throws IOException {
        for (Room room : rooms) {
            String data = room.runProject(process, input);
            if (data != null) {
                return data;
            }
        }
        throw new IOException();
    }

    public void leaveRoom(ServerProcess process) {
        int emptyRoom = -1;
        for (int i = 0; i < rooms.size(); ++i) {
            try {
                boolean result = rooms.get(i).leaveRoom(process);
                if (!result) {
                    emptyRoom = i;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (emptyRoom >= 0) {
            rooms.remove(emptyRoom);
        }
    }

    public void createNewFile(ServerProcess process, File file) throws IOException {
        for (Room room : rooms) {
            room.createFile(process, file);
        }
    }
}
