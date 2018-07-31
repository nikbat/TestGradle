
hello_world = 'Hello World!'
message = "'Nick's world"
multi_line_text = """ this is a multi line 
test """
print(hello_world[6:])
print(hello_world[:5])

print(hello_world.lower())
print(hello_world.upper())

print(hello_world.count('Hello'))
print(hello_world.count('l'))

print(hello_world.find('World'))
print(hello_world.count('Universe'))

print(hello_world.replace('World','Universe'))

greeting = 'Hello'
name = 'Nick'
full_greeting = "{greeting} , {name}!"

print(full_greeting.format(name=name, greeting=greeting))

num = 4.8
print(type(num))

num_1 ='200'
num2 = '200'

print (int(num_1) + int(num2))

num_list = []
[num_list.append(i) for i in range(10)]
print(', '.join(map(str, num_list)))
#map function converts int list to str list

numList = ['1', '2', '3', '4']
seperator = ', '
print(seperator.join(numList))

list1 = [n*n for n in num_list]
print(list1)

list2 = map(lambda n: n*n, num_list)
print(list2)

list3 = [n for n in num_list if n%2 == 0]
print(list3)

list4 = filter(lambda n: n%2==0, num_list)
print(list4)

num = [1,2,3,4]
letter = 'abcd'

list5 = [(l,n) for l in letter for n in num]
print(list5)

#tuples
tuple1 = ('Test', 1);
print(tuple1[0])
print(tuple1[1])
t1, t2 = tuple1
print(t1)
print(t2)

set1 = (n for n in num_list)
print(set1)

str1 = 'abcd'



