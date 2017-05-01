-- Teilebahn http://www.youtube.com/watch?v=21_DvRUiq6Q

	-- directions
	local DIRS = { 'N', 'S', 'E', 'W' }

	-- encoding the opposite directions
	local OPPOSITE_DIRS = { N='S', S='N', E='W', W='E' }



-------------------------- init ---------------------------------

function ai.init(map, money)	

	-- List of all junktions as {x, y, E, W, N, S, count}
	junctions = {}

	-- index of the last junction
	lastJunction = nil

	-- the direction we took at the last junction
	lastDirection = nil

	-- list of guessed directions
	guessedDirections = {}

	-- buy a train
	buyTrain(1,3,'E')
	print(getNumberOfLines())
end

function findJunction(x, y)
	for i=1, #junctions do
		if junctions[i].x == x and junctions[i].y == y then
			return i
		end
	end
	return 0
end


---------------------- choose direction ----------------------------

function ai.chooseDirection( train,possibleDirections )
	
	--print("possibleDirs: " .. to_string(possibleDirections))

	-- first, we have to adjust the coordinates according to the trains direction
	if train.dir == 'E' then
		train.x = train.x + 1
	elseif train.dir == 'W' then
		train.x = train.x - 1
	elseif train.dir == 'S' then
		train.y = train.y + 1
	elseif train.dir == 'N' then
		train.y = train.y - 1
	end

	-- check, if we know our junction
	thisJunction = findJunction(train.x, train.y)
	if thisJunction == 0 then

		-- unknown junction. add this junction to the list of junctions
		thisJunction = #junctions + 1
		junctions[thisJunction] = { x=train.x, y=train.y, count=0}
		-- print("junctions :" .. to_string(junctions))
	end


	-- add edges from and to the last junction
	if lastJunction ~= nil then
		junctions[lastJunction][lastDirection] = thisJunction 
		junctions[thisJunction][OPPOSITE_DIRS[train.dir]] = lastJunction
		--print("junctions :" .. to_string(junctions))		
	end

	-- set last junction
	lastJunction = thisJunction

	-- if we have no passenger on board, take a random direction
	if train.passenger == nil then
		repeat
			lastDirection = DIRS[random(4)]
		until possibleDirections[lastDirection]
		return lastDirection
	end

	-- 
	junctions[lastJunction].count = junctions[lastJunction].count + 1

	-- choose direction by comparing the distances of the reachable junctions to destination
	--[[bestDistance = 1000
	if possibleDirections['S'] then	
		lastDirection  = 'S'
		nextJunction = junctions[thisJunction]['S']
		if nextJunction == nil then
			bestDistance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			bestDistance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
	elseif possibleDirections['N'] then
		nextJunction = junctions[thisJunction]['N']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'N'
		end
	elseif possibleDirections['W'] then
		nextJunction = junctions[thisJunction]['W']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'W'
		end
	elseif possibleDirections['E'] then
		nextJunction = junctions[thisJunction]['E']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'E'
		end
	end]]

	-- if we're running in a circle, choose random direction
	if junctions[thisJunction].count > 2 then
		print("PANIC!")
		dropPassenger(train)

		-- validate all junctions
		for i=1,#junctions do
			junctions[i].count = 0
		end
	end

	-- choose a direction by distance heuristic
	distX = train.passenger.destX - junctions[thisJunction].x
	distY = train.passenger.destY - junctions[thisJunction].y
	eastwest = math.abs (distX) > math.abs (distY)

	-- best choice
	if eastwest then
		if distX < 0 and possibleDirections['W'] then
			lastDirection = 'W'
			return lastDirection
		elseif distX > 0 and possibleDirections['E'] then
			lastDirection = 'E'
			return lastDirection
		end
	else
		if distY < 0 and possibleDirections['N'] then
			lastDirection = 'N'
			return lastDirection
		elseif distY > 0 and possibleDirections['S'] then
			lastDirection = 'S'
			return lastDirection
		end
	end
	-- second best choice
	if eastwest then
		if distY < 0 and possibleDirections['N'] then
			lastDirection = 'N'
			return lastDirection
		elseif distY > 0 and possibleDirections['S'] then
			lastDirection = 'S'
			return lastDirection
		end
	else
		if distX < 0 and possibleDirections['W'] then
			lastDirection = 'W'
			return lastDirection
		elseif distX > 0 and possibleDirections['E'] then
			lastDirection = 'E'
			return lastDirection
		end
	end
	
	-- random choice
	repeat
		lastDirection = DIRS[random(4)]
	until possibleDirections[lastDirection]
	return lastDirection


	--print(getNumberOfLines())
end

function computeDistance (x1,y1,x2,y2)
	return math.abs (x1-x2) + math.abs (y1-y2)
end

function ai.foundPassengers(train, passengers)
	--print("foundPassengers")
	--print(getNumberOfLines())
	if #passengers == 1 then
		return passengers[1]
	end

	mindist=100
	bestPass=0

	for i=1,#passengers do
		dist = math.abs (train.x-passengers[i].destX) + math.abs (train.y-passengers[i].destY)
		if  dist < mindist then
			mindist=dist
			bestPass=i
		end
	end
	return passengers[bestPass]
end	

function ai.foundDestination(train)
	dropPassenger(train)

	-- validate all junctions
	for i=1,#junctions do
		junctions[i].count = 0
	end
end

function ai.enoughMoney(  )
	--buyTrain(2,2)
end


------------------- universal table print -------------

function table_print (tt, indent, done)
  done = done or {}
  indent = indent or 0
  if type(tt) == "table" then
    local sb = {}
    for key, value in pairs (tt) do
      table.insert(sb, string.rep (" ", indent)) -- indent it
      if type (value) == "table" and not done [value] then
        done [value] = true
        table.insert(sb, "{\n");
        table.insert(sb, table_print (value, indent + 2, done))
        table.insert(sb, string.rep (" ", indent)) -- indent it
        table.insert(sb, "}\n");
      elseif "number" == type(key) then
        table.insert(sb, string.format("\"%s\"\n", tostring(value)))
      else
        table.insert(sb, string.format(
            "%s = \"%s\"\n", tostring (key), tostring(value)))
       end
    end
    return table.concat(sb)
  else
    return tt .. "\n"
  end
end

function to_string( tbl )
    if  "nil"       == type( tbl ) then
        return tostring(nil)
    elseif  "table" == type( tbl ) then
        return table_print(tbl)
    elseif  "string" == type( tbl ) then
        return tbl
    else
        return tostring(tbl)
    end
end

