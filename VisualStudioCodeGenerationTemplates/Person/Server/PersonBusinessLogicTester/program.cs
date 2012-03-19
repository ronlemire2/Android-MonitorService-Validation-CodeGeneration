
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;
using PersonBusinessLogic;

namespace PersonTester
{
   class Program
   {
      private static PersonBusinessLogic.PersonService personService;
      private static int newPersonId;

      static void Main(string[] args)
      {
         personService = new PersonService();
         AddPersonRow();
         GetPersonViewList();
         GetPersonView(newPersonId);
         //UpdatePersonRow(newPersonId);
         //DeletePersonView(newPersonId);
      }

      private static void AddPersonRow()
      {
         PersonView personView = new PersonView();
         personView.Id = 0;
			personView.FirstName = "ZFAKY";
			personView.LastName = "SSNEK";
			personView.Email = "YLLHH";
         newPersonId = personService.SavePerson(personView);
         Console.WriteLine(string.Format("*** AddPersonRow ***"));
         Console.WriteLine(string.Format("New Id: {0}", newPersonId.ToString()));
         Console.WriteLine(Environment.NewLine);
      }

      private static void GetPersonViewList()
      {
         List<PersonView> personViewList = null;
         personViewList = personService.GetPersonViewList();
         Console.WriteLine(string.Format("*** GetPersonViewList ***"));
         Console.WriteLine(string.Format("PersonViewList count: {0}", personViewList.Count()));
         Console.WriteLine(Environment.NewLine);
      }

      private static void GetPersonView(int Id)
      {
         PersonView personView = null;
         personView = personService.GetPersonView(Id);
         Console.WriteLine(string.Format("*** GetPersonView ***"));
	      Console.WriteLine(string.Format("PersonView Id: {0}", personView.Id.ToString()));
	      Console.WriteLine(string.Format("PersonView FirstName: {0}", personView.FirstName.ToString()));
	      Console.WriteLine(string.Format("PersonView LastName: {0}", personView.LastName.ToString()));
	      Console.WriteLine(string.Format("PersonView Email: {0}", personView.Email.ToString()));
        
         Console.WriteLine(Environment.NewLine);
      }

      private static void UpdatePersonRow(int Id)
      {
         // Get PersonView
         PersonView personView = null;
         personView = personService.GetPersonView(Id);
         Console.WriteLine(string.Format("*** UpdatePersonRow ***"));
	      Console.WriteLine(string.Format("Original PersonView Id: {0}", personView.Id.ToString()));
	      Console.WriteLine(string.Format("Original PersonView FirstName: {0}", personView.FirstName.ToString()));
	      Console.WriteLine(string.Format("Original PersonView LastName: {0}", personView.LastName.ToString()));
	      Console.WriteLine(string.Format("Original PersonView Email: {0}", personView.Email.ToString()));
   
         // Change PersonView
			personView.FirstName = "QZQFR";
			personView.LastName = "UUKEQ";
			personView.Email = "HZXHN";
         // Save Person
         personService.SavePerson(personView);

         // Get updated PersonView
         personView = personService.GetPersonView(Id);
	      Console.WriteLine(string.Format("Updated PersonView Id: {0}", personView.Id.ToString()));
	      Console.WriteLine(string.Format("Updated PersonView FirstName: {0}", personView.FirstName.ToString()));
	      Console.WriteLine(string.Format("Updated PersonView LastName: {0}", personView.LastName.ToString()));
	      Console.WriteLine(string.Format("Updated PersonView Email: {0}", personView.Email.ToString()));
  
         Console.WriteLine(Environment.NewLine);
      }

      private static void DeletePersonView(int Id)
      {
         // Get count before delete
         List<PersonView> personViewList = null;
         personViewList = personService.GetPersonViewList();

         Console.WriteLine(string.Format("*** DeletePersonView ***"));
         Console.WriteLine(string.Format("Before delete count: {0}", personViewList.Count())); 
         // Delete Person 
         int rowsDeleted = personService.DeletePerson(Id);

         // Get count after delete
         personViewList = null;
         personViewList = personService.GetPersonViewList();
         Console.WriteLine(string.Format("After delete count: {0}", personViewList.Count()));
         Console.WriteLine(Environment.NewLine);
         Console.WriteLine("Press any key to continue...");
         Console.Read();
      }
   }
}


