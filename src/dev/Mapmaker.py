import pygame
pygame.init()
pygame.font.init()

NUM_OF_TILES = 60
SCREEN_SIZE = 1250
WALL_MAX_HEALTH = 240
TILE_SIZE = SCREEN_SIZE/NUM_OF_TILES
COLOR_DICT = {'x': (255, 255, 255), 'u' : (120, 120, 120), 'm' : (138, 88, 8), 'w' : (0, 120, 171), 'l' : (240, 17, 0)}

running = True
array2d = []

def setArr(): #sets array2d to NUM_OF_TILES x NUM_OF_TILES
    for i in range(NUM_OF_TILES):
        array2d.append([])
        for j in range(NUM_OF_TILES):
            array2d[i].append(None)
def clear(): #sets array2d to clear map
    for i in range(NUM_OF_TILES):
        for j in range(NUM_OF_TILES):
            if(i == 0 or i == NUM_OF_TILES-1 or j == 0 or j == NUM_OF_TILES-1):
                array2d[i][j] = ['u', WALL_MAX_HEALTH]
            else:
                array2d[i][j] = ['x', 0]
def load(n): #sets array2d to file contents of n.txt
    try:
        with open("src/dev/" + n, "r") as file:
            contents = file.readlines()
            for indexI, i in enumerate(contents):
                i = i.rstrip()
                for indexJ, j in enumerate(i.split(" ")):
                    array2d[indexI][indexJ] = [j[0], int(j[1:])]
        print("Successful load from " + n)
    except FileNotFoundError:
        print("file not found, create file by writing")

def arrToString(): #return a string representation of the array
    string = ""
    for i in range(NUM_OF_TILES):
        for j in range(NUM_OF_TILES):
            string += str(array2d[i][j][0]) + str(array2d[i][j][1]) + " "
        string += "\n"
    return string

def printArr():
    print(arrToString(), end="")

def writeArr(n): #n is which file to write
    with open("src/dev/" + n, "w") as file:
        file.write(arrToString())
    print("Successful write to " + n)

def text(size, message, color, textx, texty, align = "left", font = "Comic Sans MS"):
    myfont = pygame.font.SysFont(font, size)
    text_width, text_height = myfont.size(message)
    text_surface = myfont.render(message, True, color)
    if align == "left":
        screen.blit(text_surface, (textx, texty - (text_height/2)))
    if align == "center":
        screen.blit(text_surface, (textx - (text_width/2), texty - (text_height/2)))
    if align == "right":
        screen.blit(text_surface, (textx - text_width, texty - (text_height/2)))

#initialization
setArr()
screen = pygame.display.set_mode((SCREEN_SIZE, SCREEN_SIZE))
fileName = "0.txt" #should always be a string
load(fileName)
pygame.display.set_caption(fileName)

while running:
    screen.fill((255, 255, 255))
    mouse_down = False
    mouse_position = pygame.mouse.get_pos()
    pressed_keys = pygame.key.get_pressed()
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.MOUSEBUTTONDOWN:
            mouse_down = True
        if event.type == pygame.KEYDOWN:
            #commands
            if pressed_keys[pygame.K_LCTRL] or pressed_keys[pygame.K_RCTRL]:
                if event.key == pygame.K_w: #c + write
                    writeArr(fileName)
                elif event.key == pygame.K_l: #c + load
                    load(fileName)
                elif event.key == pygame.K_c: #c + clear
                    clear()
                elif event.key == pygame.K_p: #c + print
                    printArr()
    
                #set current text file
                elif(event.key >= pygame.K_0 and event.key <= pygame.K_9):
                    fileName = str(event.key - pygame.K_0) + ".txt"
                    pygame.display.set_caption(fileName)

    #draw map
    for i in range(NUM_OF_TILES):
        for j in range(NUM_OF_TILES):
            if(array2d[i][j][0] != 'x'):
                #dont draw color if is blank
                pygame.draw.rect(screen, COLOR_DICT[array2d[i][j][0]], (j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE+1, TILE_SIZE+1))
            if(array2d[i][j][1] != 0) and (array2d[i][j][0] != 'u'):
                #dont draw text if is 0 or is unbreakable
                text(10, str(array2d[i][j][1]), (0, 0, 0), j*TILE_SIZE + 10, i*TILE_SIZE + 10, "center")

    if(mouse_down):
        y = int(mouse_position[1]/TILE_SIZE)
        x = int(mouse_position[0]/TILE_SIZE)

        #terrain
        if(pressed_keys[pygame.K_u]): #unbreakables
            array2d[y][x][0] = 'u'
            array2d[y][x][1] = WALL_MAX_HEALTH
        elif(pressed_keys[pygame.K_w]): #water
            array2d[y][x][0] = 'w'
        elif(pressed_keys[pygame.K_m]): #mud
            array2d[y][x][0] = 'm'
        elif(pressed_keys[pygame.K_l]): #mud
            array2d[y][x][0] = 'l'

        #increment health if not unbreakable
        elif(pressed_keys[pygame.K_a] and array2d[y][x][0] != 'u'):
            array2d[y][x][1] += 1
        elif(pressed_keys[pygame.K_s] and array2d[y][x][0] != 'u'):
            array2d[y][x][1] += 10
        elif(pressed_keys[pygame.K_d] and array2d[y][x][0] != 'u'):
            array2d[y][x][1] += 40
    
        else:
            array2d[y][x][0] = 'x'
            array2d[y][x][1] = 0

        if array2d[y][x][1] > WALL_MAX_HEALTH or array2d[y][x][1] < 0: #ceiling and floor health
            array2d[y][x][1] = 0
        
    
    pygame.display.update()

pygame.quit()