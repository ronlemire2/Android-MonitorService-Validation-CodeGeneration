using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;

namespace PersonDataAccess
{
   public class PersonRepository : RepositoryBase, IPersonRepository
   {
      public IEnumerable<Person> GetPersonList
      {
         get
         {
            var entityList = default(IEnumerable<Person>);

            // Calling data access (entity framework)
            entityList = _dbContext.People;

            return entityList;
         }
      }

      public Person GetPerson(int Id)
      {
         var result = default(Person);

         // Calling data access (entity framework)
         result = _dbContext.People
            .Where(d => d.Id == Id).FirstOrDefault();

         return result;
      }

      public int SetPerson(Person entity)
      {
         var result = 0;

         // Load object into context (entity framework) 
         var loadedEntity = _dbContext.People.Where(d => d.Id == entity.Id).FirstOrDefault();

         // Modify the context
         if (loadedEntity == null)
         { //not found?
            // Add
            this._dbContext.People.AddObject(entity);
         }
         else
         {
            // Update
            this._dbContext.People.ApplyCurrentValues(entity);
         }

         // Save in data access (entity framework)
         result = this._dbContext.SaveChanges();

         // 2/25/2012 return entity.Id if add
         //return result;
         return loadedEntity == null ? entity.Id : result;
      }

      public int DeletePerson(int Id)
      {
         int result = 0;

         // Calling data access (entity framework)
         var person = GetPerson(Id);
         _dbContext.DeleteObject(person);
         // Save in data access (entity framework)
         result = this._dbContext.SaveChanges();

         return result;
      }

   }

}
