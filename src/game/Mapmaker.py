import pygame
pygame.init()
pygame.font.init()
NUM_OF_TILES = 60
SCREEN_SIZE = 700
screen = pygame.display.set_mode((SCREEN_SIZE, SCREEN_SIZE))
tileSize = SCREEN_SIZE/NUM_OF_TILES
colorDict = {'x': (255, 255, 255), 'u' : (120, 120, 120), 'm' : (138, 88, 8), 'w' : (0, 120, 171)}
running = True

array2d = []
for i in range(NUM_OF_TILES):
    array2d.append([])
    for j in range(NUM_OF_TILES):
        if(i == 0 or i == NUM_OF_TILES-1 or j == 0 or j == NUM_OF_TILES-1):
            array2d[i].append(['u', 1])
        else:
            array2d[i].append(['x', 0]) 

def printArr():
    for i in range(NUM_OF_TILES):
        for j in range(NUM_OF_TILES):
            print(str(array2d[i][j][0]) + str(array2d[i][j][1]) + " ", end="")
        print()

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


while running:
    screen.fill((255, 255, 255))
    mouse_down = False
    mouse_position = pygame.mouse.get_pos()
    pressed_keys = pygame.key.get_pressed()
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            running = False
        if event.type == pygame.MOUSEBUTTONDOWN:
            mouse_down = True
    if pressed_keys[pygame.K_RETURN]:
        printArr()

    for i in range(NUM_OF_TILES):
        for j in range(NUM_OF_TILES):
            if(array2d[i][j][0] != 'x'):
                #dont draw color if is blank
                pygame.draw.rect(screen, colorDict[array2d[i][j][0]], (j*tileSize, i*tileSize, tileSize+1, tileSize+1))
            if(array2d[i][j][1] != 0) and (array2d[i][j][0] != 'u'):
                #dont draw if is 0 or is unbreakable
                text(10, str(array2d[i][j][1]), (0, 0, 0), j*tileSize + 5, i*tileSize + 5, "center")

    if(mouse_down):
        y = int(mouse_position[1]/tileSize)
        x = int(mouse_position[0]/tileSize)

        #terrain
        if(pressed_keys[pygame.K_u]): #unbreakables
            array2d[y][x][0] = 'u'
            array2d[y][x][1] = 1
        elif(pressed_keys[pygame.K_w]): #water
            array2d[y][x][0] = 'w'
            array2d[y][x][1] = 0
        elif(pressed_keys[pygame.K_m]): #mud
            array2d[y][x][0] = 'm'
            array2d[y][x][1] = 0

        #increment health if not unbreakable
        elif(array2d[y][x][0] != 'u'):
            if(pressed_keys[pygame.K_a]):
                array2d[y][x][1] += 1
            if(pressed_keys[pygame.K_s]):
                array2d[y][x][1] += 10
            if(pressed_keys[pygame.K_d]):
                array2d[y][x][1] += 40
        
        else:
            array2d[y][x][0] = 'x'
            array2d[y][x][1] = 0

        if array2d[y][x][1] > 240: #ceiling health
            array2d[y][x][1] = 0
        
    
    pygame.display.update()